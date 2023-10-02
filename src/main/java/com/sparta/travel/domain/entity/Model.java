package com.sparta.travel.domain.entity;

import com.sparta.travel.domain.dto.CategoryGroupCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Model {
    private String query;
    private String region;
    private String keyword;
    private CategoryGroupCode group;

}
