package com.sparta.travel.domain.dto;

import com.sparta.travel.domain.entity.Place;
import com.sparta.travel.domain.entity.Plan;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PlanResponseDto {
    private Long id;
    private String userId;
    private LocalDate date;
    private String city;
    private List<PlaceResponseDto> placeList;

    public PlanResponseDto(Plan plan, String userId, List<PlaceResponseDto> list) {
        this.id = plan.getId();
        this.userId = userId;
        this.date = plan.getDate();
        this.city = plan.getCity();
        this.placeList = list;
    }
}
