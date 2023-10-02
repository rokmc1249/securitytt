package com.sparta.travel.domain.service;

import com.sparta.travel.domain.dto.ApiResponseDto;
import com.sparta.travel.domain.dto.CategoryGroupCode;
import com.sparta.travel.domain.entity.Area;
import com.sparta.travel.domain.entity.Model;
import com.sparta.travel.domain.repository.AreaRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class KakaoApiService {
    private final RestTemplate restTemplate;
    private final AreaRepository areaRepository;

    public KakaoApiService(RestTemplateBuilder builder, AreaRepository areaRepository) {
        this.restTemplate = builder.build();
        this.areaRepository = areaRepository;
    }

    public List<String> regionSearch(String query){
        List<Area> nameList = areaRepository.findAllByName(query);

        List<String> regionList = new ArrayList<>();
        for(Area area:nameList){
            regionList.add(area.getRegion());
        }
        return regionList;
    }

    public List<ApiResponseDto> categorySearch(String query) {
        URI mapUri = UriComponentsBuilder
                .fromUriString(	"https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", query)
                .queryParam("category_group_code", CategoryGroupCode.AT4)
                .queryParam("size",7)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> mapRequestEntity = RequestEntity
                .get(mapUri)
                .header("Authorization","KakaoAK a463e134a672b701c0ddefd3519e904e")
                .build();

        ResponseEntity<String> mapResponseEntity = restTemplate.exchange(mapRequestEntity, String.class);

        List<ApiResponseDto> apiResponseDtoList = fromJSONtoMap(mapResponseEntity.getBody());

        List<ApiResponseDto> totalDtoList = new ArrayList<>();
        for(ApiResponseDto apiResponseDto : apiResponseDtoList){
            String placeName = apiResponseDto.getPlace_name();
            URI imgUri = UriComponentsBuilder
                    .fromUriString(	"https://dapi.kakao.com")
                    .path("/v2/search/image")
                    .queryParam("query", placeName)
                    .encode()
                    .build()
                    .toUri();

            RequestEntity<Void> imgRequestEntity = RequestEntity
                    .get(imgUri)
                    .header("Authorization","KakaoAK a463e134a672b701c0ddefd3519e904e")
                    .build();

            ResponseEntity<String> imgResponseEntity = restTemplate.exchange(imgRequestEntity, String.class);

            totalDtoList.add(fromJSONtoImage(imgResponseEntity.getBody(), apiResponseDto));
        }
        return totalDtoList;
    }

    public List<ApiResponseDto> categoryDetailSearch(Model model) {
        String total = model.getQuery()+(model.getRegion()!=null ? model.getRegion() : "");

        URI mapUri = UriComponentsBuilder
                .fromUriString(	"https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", total)
                .queryParam("category_group_code", model.getGroup())
                .queryParam("size",7)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> mapRequestEntity = RequestEntity
                .get(mapUri)
                .header("Authorization","KakaoAK a463e134a672b701c0ddefd3519e904e")
                .build();

        ResponseEntity<String> mapResponseEntity = restTemplate.exchange(mapRequestEntity, String.class);

        List<ApiResponseDto> apiResponseDtoList = fromJSONtoMap(mapResponseEntity.getBody());

        List<ApiResponseDto> totalDtoList = new ArrayList<>();
        for(ApiResponseDto apiResponseDto : apiResponseDtoList){
            String placeName = apiResponseDto.getPlace_name();
            URI imgUri = UriComponentsBuilder
                    .fromUriString(	"https://dapi.kakao.com")
                    .path("/v2/search/image")
                    .queryParam("query", placeName)
                    .encode()
                    .build()
                    .toUri();

            RequestEntity<Void> imgRequestEntity = RequestEntity
                    .get(imgUri)
                    .header("Authorization","KakaoAK a463e134a672b701c0ddefd3519e904e")
                    .build();

            ResponseEntity<String> imgResponseEntity = restTemplate.exchange(imgRequestEntity, String.class);

            totalDtoList.add(fromJSONtoImage(imgResponseEntity.getBody(), apiResponseDto));
        }
        return totalDtoList;
    }
    public List<ApiResponseDto> keywordDetailSearch(Model model) { //키워드 상세검색
        String total = model.getQuery()+model.getKeyword()+(model.getRegion()!=null ? model.getRegion() : "");

        URI mapUri = UriComponentsBuilder
                .fromUriString(	"https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", total)
                .queryParam("size",7)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> mapRequestEntity = RequestEntity
                .get(mapUri)
                .header("Authorization","KakaoAK a463e134a672b701c0ddefd3519e904e")
                .build();

        ResponseEntity<String> mapResponseEntity = restTemplate.exchange(mapRequestEntity, String.class);

        List<ApiResponseDto> apiResponseDtoList = fromJSONtoMap(mapResponseEntity.getBody());

        List<ApiResponseDto> totalDtoList = new ArrayList<>();
            for(ApiResponseDto apiResponseDto:apiResponseDtoList) {
                String placeName = apiResponseDto.getPlace_name();
                URI imgUri = UriComponentsBuilder
                        .fromUriString("https://dapi.kakao.com")
                        .path("/v2/search/image")
                        .queryParam("query", placeName)
                        .encode()
                        .build()
                        .toUri();

                RequestEntity<Void> imgRequestEntity = RequestEntity
                        .get(imgUri)
                        .header("Authorization", "KakaoAK a463e134a672b701c0ddefd3519e904e")
                        .build();

                ResponseEntity<String> imgResponseEntity = restTemplate.exchange(imgRequestEntity, String.class);

                totalDtoList.add(fromJSONtoImage(imgResponseEntity.getBody(), apiResponseDto));
            }
        return totalDtoList;
    }

    public List<ApiResponseDto> fromJSONtoMap(String responseEntity)  {

        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray infos  = jsonObject.getJSONArray("documents");
        List<ApiResponseDto> apiResponseDtoList = new ArrayList<>();

        for (Object info : infos) {
            ApiResponseDto apiResponseDto = new ApiResponseDto((JSONObject) info);
            apiResponseDtoList.add(apiResponseDto);
        }

        return apiResponseDtoList;
    }
    public ApiResponseDto fromJSONtoImage(String responseEntity, ApiResponseDto apiResponseDto) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray infos  = jsonObject.getJSONArray("documents");
        ApiResponseDto dto = new ApiResponseDto();
        for (Object info : infos) {
            dto = new ApiResponseDto((JSONObject) info, apiResponseDto);
        }
        return dto;

    }
}
