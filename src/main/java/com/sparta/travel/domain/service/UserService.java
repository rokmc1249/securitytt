package com.sparta.travel.domain.service;

import com.sparta.travel.domain.dto.MsgResponseDto;
import com.sparta.travel.domain.dto.ProfileRequestDto;
import com.sparta.travel.domain.dto.ProfileResponseDto;
import com.sparta.travel.domain.dto.SignupRequestDto;
import com.sparta.travel.domain.entity.Bookmark;
import com.sparta.travel.domain.entity.Plan;
import com.sparta.travel.domain.entity.RefreshToken;
import com.sparta.travel.domain.entity.User;
import com.sparta.travel.domain.jwt.JwtUtil;
import com.sparta.travel.domain.jwt.UserRoleEnum;
import com.sparta.travel.domain.repository.BookmarkRepository;
import com.sparta.travel.domain.repository.PlanRepository;
import com.sparta.travel.domain.repository.RefreshTokenRepository;
import com.sparta.travel.domain.repository.UserRepository;
import com.sparta.travel.domain.s3.S3Uploader;
import com.sparta.travel.global.CustomException;
import com.sparta.travel.global.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookmarkRepository bookmarkRepository;
    private final PlanRepository planRepository;
    private final JwtUtil jwtUtil;
    private final S3Uploader s3Uploader;
    private final RefreshTokenRepository refreshTokenRepository;


    public MsgResponseDto signup(SignupRequestDto requestDto) { // 프론트랑 합칠 시 변경
        String userId = requestDto.getUserId();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkUser = userRepository.findByUserId(userId); //ID 중복확인
        if(checkUser.isPresent()){
            throw new CustomException(ErrorCode.DUPLICATED_ID);
        }

        String email = requestDto.getEmail();
        checkEmail(email); // 이메일 중복확인

        String nickname = requestDto.getNickname();
        checkNickname(nickname); // 닉네임 중복확인

        //UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(userId,password,email,nickname);
//        User user = new User(userId,password,role,email,nickname);
        userRepository.save(user);
        return new MsgResponseDto(HttpServletResponse.SC_OK, "회원가입이 성공했습니다.");
    }

    public MsgResponseDto deleteUser(User user) {
        User deleteUser = checkUser(user);

        List<Bookmark> bookmarkList = bookmarkRepository.findByUser(deleteUser); // 연관된 북마크 삭제
        bookmarkRepository.deleteAll(bookmarkList);

        List<Plan> planList = planRepository.findByUser(deleteUser); // 연관된 일정 삭제
        planRepository.deleteAll(planList);

        List<RefreshToken> refreshTokenList = refreshTokenRepository.findByUser(deleteUser);
        refreshTokenRepository.deleteAll(refreshTokenList);

        userRepository.delete(deleteUser); // 유저 삭제
        return new MsgResponseDto(HttpServletResponse.SC_OK, "탈퇴 완료!!");

    }

    public MsgResponseDto updateProfile(ProfileRequestDto requestDto, User user) {
        User updateUser = checkUser(user);
        String email = requestDto.getEmail();
        checkEmail(email); // 이메일 중복확인
        String nickname = requestDto.getNickname();
        checkNickname(nickname); // 닉네임 중복확인

        updateUser.update(email,nickname);

        return new MsgResponseDto(HttpServletResponse.SC_OK, "프로필수정이 성공했습니다.");
    }

    public ProfileResponseDto getProfile(User user) {
        User getUser = userRepository.findByUserId(user.getUserId()).
                orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        return new ProfileResponseDto(getUser);
    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request) {

                // 클라이언트의 리프레쉬 토큰을 헤더에서 추출
                String refreshToken = jwtUtil.extractRefreshToken(request);

                User user = userRepository.findByUserId(jwtUtil.getUserInfoFromToken(refreshToken).getSubject())
                        .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
                if (StringUtils.hasText(refreshToken)) {

                        if (jwtUtil.validateToken(refreshToken)) {
                                // 리프레쉬 토큰이 유효하다면 새로운 엑세스 토큰 발급
//                                String newAccessToken = jwtUtil.createToken(user.getUserId(), UserRoleEnum.USER);
                                  String newAccessToken = jwtUtil.createToken(user.getUserId());

                                // 기존의 리프레쉬 토큰 삭제
                                RefreshToken existToken = refreshTokenRepository.findByTokenValueAndUser(refreshToken, user);
                                if (existToken != null) {
                                        refreshTokenRepository.delete(existToken);
                                }

                                // 리프레쉬 토큰 재발급 및 저장
                                // String newRefreshToken = jwtUtil.createRefreshToken(user.getUserId(), UserRoleEnum.USER);
                                String newRefreshToken = jwtUtil.createRefreshToken(user.getUserId());
                                RefreshToken refresh = new RefreshToken(newRefreshToken, user);
                                refreshTokenRepository.save(refresh);

                                // 헤더에 엑세스 & 리프레쉬 토큰 넣기
                                HttpHeaders headers = new HttpHeaders();
                                headers.set("Authorization", newAccessToken);
                                headers.set("Refresh-Token", newRefreshToken);

                                return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
                        }
                }

                // 리프레쉬 토큰이 유효하지 않은 경우
                RefreshToken existToken = refreshTokenRepository.findByTokenValueAndUser(refreshToken, user);

                // 기존의 리프레쉬 토큰 삭제
                if (existToken != null) {
                        refreshTokenRepository.delete(existToken);
                }

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    public MsgResponseDto updateProfileImg(MultipartFile image, User user) throws IOException {

        User user1 = checkUser(user);
        if(user1.getProfile_img_url() != null) { // 사용자가 사진이 있으면
            s3Uploader.deleteFile(user1.getProfile_img_url()); // 지우기
        }

        if (!image.isEmpty()) { // 이미지가 있으면
            String  fileName = s3Uploader.upload(image, "travel");
            user1.updateProfileImg(fileName);
            return new MsgResponseDto(HttpServletResponse.SC_OK, "프로필사진 수정 및 등록 성공했습니다.", fileName);
        } else {
            throw new CustomException(ErrorCode.IMG_NULL);
        }
    }

    public void checkEmail (String email) {
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    public User checkUser (User user) {
        return userRepository.findByUserId(user.getUserId()).
                orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
    }

    public void checkNickname (String nickname) {
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }
}

