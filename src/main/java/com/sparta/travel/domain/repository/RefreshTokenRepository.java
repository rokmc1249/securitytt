package com.sparta.travel.domain.repository;

import com.sparta.travel.domain.entity.RefreshToken;
import com.sparta.travel.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken findByTokenValueAndUser(String tokenValue, User user);

}
