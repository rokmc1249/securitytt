package com.sparta.travel.domain.repository;

import com.sparta.travel.domain.entity.Bookmark;
import com.sparta.travel.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    List<Bookmark> findByUser(User user);

    List<Bookmark> findByUserAndCity(User user,String city);
}
