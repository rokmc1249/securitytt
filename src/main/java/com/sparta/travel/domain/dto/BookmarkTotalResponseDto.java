package com.sparta.travel.domain.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter

public class BookmarkTotalResponseDto {
    private final Set<String> cityList;
    private final List<BookmarkResponseDto> bookmarkList;
}
