package com.sparta.travel.domain.repository;

import com.sparta.travel.domain.entity.RefreshToken;
import com.sparta.travel.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken findByTokenValueAndUser(String tokenValue, User user);

    List<RefreshToken> findByUser(User deleteUser);

}
