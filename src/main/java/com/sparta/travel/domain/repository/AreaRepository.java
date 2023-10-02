package com.sparta.travel.domain.repository;

import com.sparta.travel.domain.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area,Long> {
    List<Area> findAllByName(String name);
}
