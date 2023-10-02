package com.sparta.travel.domain.dto;

import com.sparta.travel.domain.entity.Place;
import com.sparta.travel.domain.entity.User;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PlanRequestDto {
    private Long id;
    private String userId;
    private LocalDate date;
    private String city;
    private List<Place> placeList;
}
