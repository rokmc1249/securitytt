package com.sparta.travel.domain.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.travel.global.CustomException;
import com.sparta.travel.global.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;




    public String upload(MultipartFile uploadFile, String dirName) { // 이미지 url 업로드
        String fileName = dirName + "/" + uploadFile.getOriginalFilename();
        String uploadImageUrl = putS3(uploadFile, fileName); // S3에 이미지 넣기
        return uploadImageUrl;

    }

    private String putS3(MultipartFile uploadFile, String fileName) { // S3에 이미지 파일 넣기
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.S3_NOT_UPLOAD);
        }
    }



    public void deleteFile(String fileUrl) throws UnsupportedEncodingException {
        String fileName;
        fileName = URLDecoder.decode(fileUrl.replace("https://hang99-travel-bucket.s3.ap-northeast-2.amazonaws.com/", ""), "UTF-8");
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
}
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class S3Uploader {
//    private final AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//
//    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
//
//        return upload(uploadFile, dirName);
//    }
//
//    private String upload(File uploadFile, String dirName) { // 이미지 url 업로드
//        String fileName = dirName + "/" + uploadFile.getName();
//        String uploadImageUrl = putS3(uploadFile, fileName); // S3에 이미지 넣기
//        removeNewFile(uploadFile); // 업로드 파일 지우기
//        return uploadImageUrl;
//    }
//
//    private String putS3(File uploadFile, String fileName) { // S3에 이미지 파일 넣기
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
//        return amazonS3Client.getUrl(bucket, fileName).toString();
//    }
//
//    private void removeNewFile(File targetFile) {
//        if (targetFile.delete()) {
//            log.info("파일이 삭제되었습니다.");
//        } else {
//            log.info("파일이 삭제되지 못했습니다.");
//        }
//    }
//
//    private Optional<File> convert(MultipartFile file) throws IOException { // 파일 변환
//        File convertFile = new File(file.getOriginalFilename()); // 원본 파일 이름 가져와서 변환
//        System.out.println("convertFile = " + convertFile);
//        if(convertFile.createNewFile()) { // 새로운 파일이 생성되었는지 확인
//            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                fos.write(file.getBytes());
//            }
//            return Optional.of(convertFile);
//        }
//        return Optional.empty();
//    }
//
//    public void deleteFile(String fileUrl) throws UnsupportedEncodingException {
//        String fileName;
//        fileName = URLDecoder.decode(fileUrl.replace("https://hang99-travel-bucket.s3.ap-northeast-2.amazonaws.com/", ""), "UTF-8");
//        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
//    }
//}