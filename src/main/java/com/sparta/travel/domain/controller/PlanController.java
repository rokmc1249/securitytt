package com.sparta.travel.domain.controller;

import com.sparta.travel.domain.dto.MsgResponseDto;
import com.sparta.travel.domain.dto.PlanMsgResponseDto;
import com.sparta.travel.domain.dto.PlanRequestDto;
import com.sparta.travel.domain.dto.PlanResponseDto;
import com.sparta.travel.domain.security.UserDetailsImpl;
import com.sparta.travel.domain.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlanController {
    private final PlanService planService;

    @PostMapping("/schedule")
    public PlanMsgResponseDto createPlan(@RequestBody PlanRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return planService.createPlan(requestDto, userDetails.getUser());
    }

    @GetMapping("/mytravel")
    public List<PlanResponseDto> getPlan(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return planService.getPlan(userDetails.getUser());
    }
    @GetMapping("/mytravel/{planId}")
    public List<PlanResponseDto> getOnePlan(@PathVariable Long planId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return planService.getOnePlan(planId, userDetails.getUser());
    }

    @PutMapping("/schedule/{plan_id}")
    public MsgResponseDto updatePlan(@PathVariable Long plan_id, @RequestBody PlanRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return planService.updatePlan(plan_id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/schedule/{plan_id}")
    public MsgResponseDto deletePlan(@PathVariable Long plan_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return planService.deletePlan(plan_id, userDetails.getUser());
    }
}

