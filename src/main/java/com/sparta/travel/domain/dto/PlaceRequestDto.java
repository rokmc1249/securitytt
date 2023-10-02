package com.sparta.travel.domain.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class PlaceRequestDto {
    private  Long id;
    private String place_name;
    private String address_name;
    private String road_address_name;
    private String x;
    private String y;
    private String img_url;
}
