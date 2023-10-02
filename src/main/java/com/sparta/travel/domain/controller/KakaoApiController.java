package com.sparta.travel.domain.controller;

import com.sparta.travel.domain.dto.ApiResponseDto;
import com.sparta.travel.domain.entity.Model;
import com.sparta.travel.domain.service.KakaoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    @GetMapping("/findregion/{query}")
    public List<String> regionSearch(@PathVariable String query){
        return kakaoApiService.regionSearch(query);
    }

    @GetMapping("/findplace/{query}")
    public List<ApiResponseDto> categorySearch(@PathVariable String query)  {
        return kakaoApiService.categorySearch(query);
    }

    @GetMapping("/findplace/group")
    public List<ApiResponseDto> categoryDetailSearch(@ModelAttribute Model model)  {
        return kakaoApiService.categoryDetailSearch(model);
    }

    @GetMapping("/findplace/keyword")
    public List<ApiResponseDto> keywordDetailSearch(@ModelAttribute Model model)  {
        return kakaoApiService.keywordDetailSearch(model);
    }

}

