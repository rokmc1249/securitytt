package com.sparta.travel.domain.repository;

import com.sparta.travel.domain.entity.Plan;
import com.sparta.travel.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan,Long> {
    List<Plan> findByUser(User user);
}
