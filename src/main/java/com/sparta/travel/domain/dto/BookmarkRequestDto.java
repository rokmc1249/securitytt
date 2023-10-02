package com.sparta.travel.domain.dto;

import lombok.Getter;

@Getter
public class BookmarkRequestDto {
    private String place_name;
    private String address_name;
    private String road_address_name;
    private String x;
    private String y;
    private String city;
    private String group_name;
    private String img_url;
}
