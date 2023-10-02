package com.sparta.travel.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Getter
@NoArgsConstructor
public class ApiResponseDto {
    private String place_name;
    private String address_name;
    private String road_address_name;
    private String x;
    private String y;
    private String group_name;
    private String img_url;





    public ApiResponseDto(JSONObject infoFromJson){
        this.place_name = infoFromJson.getString("place_name");
        this.address_name = infoFromJson.getString("address_name");
        this.road_address_name = infoFromJson.getString("road_address_name");
        this.x = infoFromJson.getString("x");
        this.y = infoFromJson.getString("y");
        this.group_name = infoFromJson.getString("category_group_name");

    }

    public ApiResponseDto(JSONObject infoFromJson, ApiResponseDto apiResponseDto) {
        this.place_name = apiResponseDto.getPlace_name();
        this.address_name = apiResponseDto.getAddress_name();
        this.road_address_name = apiResponseDto.getRoad_address_name();
        this.x = apiResponseDto.getX();
        this.y = apiResponseDto.getY();
        this.group_name = apiResponseDto.getGroup_name();
        this.img_url = infoFromJson.getString("image_url");
    }
}
