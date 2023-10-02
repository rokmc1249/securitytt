package com.sparta.travel.domain.repository;

import com.sparta.travel.domain.dto.PlanResponseDto;
import com.sparta.travel.domain.entity.Place;
import com.sparta.travel.domain.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByPlanId(Long id);
}
