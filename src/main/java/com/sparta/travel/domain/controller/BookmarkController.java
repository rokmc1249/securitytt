package com.sparta.travel.domain.controller;

import com.sparta.travel.domain.dto.BookmarkRequestDto;
import com.sparta.travel.domain.dto.BookmarkResponseDto;
import com.sparta.travel.domain.dto.BookmarkTotalResponseDto;
import com.sparta.travel.domain.dto.MsgResponseDto;
import com.sparta.travel.domain.security.UserDetailsImpl;
import com.sparta.travel.domain.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/bookmark")
    public ResponseEntity<BookmarkTotalResponseDto> getAllBookMark(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(bookmarkService.getAllBookmark(userDetails.getUser()));
    }

    @GetMapping ("/bookmark/{city}")
    public ResponseEntity<BookmarkTotalResponseDto>getCityBookMark(@PathVariable String city, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(bookmarkService.getCityBookMark(city, userDetails.getUser()));
    }

    @PostMapping("/bookmark")
    public ResponseEntity<MsgResponseDto>createBookMark(@RequestBody BookmarkRequestDto bookmarkRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(bookmarkService.createBookMark(bookmarkRequestDto, userDetails.getUser()));
    }

    @DeleteMapping("/bookmark/{id}")
    public ResponseEntity<MsgResponseDto>deleteBookMark(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(bookmarkService.deleteBookMark(id, userDetails.getUser()));
    }


}
