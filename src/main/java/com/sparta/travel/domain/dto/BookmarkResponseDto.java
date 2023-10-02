package com.sparta.travel.domain.dto;

import com.sparta.travel.domain.entity.Bookmark;
import lombok.Getter;

import java.util.List;

@Getter
public class BookmarkResponseDto {
    private Long id;
    private String UserId;
    private String place_name;
    private String address_name;
    private String road_address_name;
    private String x;
    private String y;
    private String group_name;
    private String image_url;
    private String city;


    public BookmarkResponseDto(Bookmark bookmark) {
        this.id = bookmark.getId();
        this.UserId = bookmark.getUser().getUserId();
        this.place_name = bookmark.getPlace_name();
        this.address_name = bookmark.getAddress_name();
        this.road_address_name = bookmark.getRoad_address_name();
        this.x = bookmark.getX();
        this.y = bookmark.getY();
        this.group_name = bookmark.getGroup_name();
        this.image_url = bookmark.getImg_url();
        this.city = bookmark.getCity();
    }
}
