package com.sparta.travel.domain.service;

import com.sparta.travel.domain.dto.*;
import com.sparta.travel.domain.entity.Place;
import com.sparta.travel.domain.entity.Plan;
import com.sparta.travel.domain.entity.User;
import com.sparta.travel.domain.repository.PlaceRepository;
import com.sparta.travel.domain.repository.PlanRepository;
import com.sparta.travel.global.CustomException;
import com.sparta.travel.global.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlaceRepository placeRepository;

    public PlanMsgResponseDto createPlan(PlanRequestDto requestDto, User user) {
        if(requestDto.getId() != null) {
            Plan DelPlan = planRepository.findById(requestDto.getId()).orElseThrow(() ->
                    new CustomException(ErrorCode.PLAN_NOT_FOUND)
            );
            planRepository.delete(DelPlan);
        }
        Plan plan = planRepository.save(new Plan(requestDto, user));
        List<Place> placeList = new ArrayList<>();
        for(Place place : requestDto.getPlaceList()){
            place.setPlan(plan);
            if(place.getGroup_name()==null||place.getGroup_name().isEmpty()) {
                place.setGroup_name("기타");
            }
            placeList.add(place);
        }

        if(placeList.isEmpty()){
            throw new CustomException(ErrorCode.PLAN_NOT_FOUND);
        } else {
            placeRepository.saveAll(placeList);
        }

        return new PlanMsgResponseDto(HttpServletResponse.SC_OK, "여행일정 저장 성공했습니다.", plan.getId());
    }

    public List<PlanResponseDto> getPlan(User user) {
        List<Plan> planList = planRepository.findByUser(user);
        List<PlanResponseDto> placeList = new ArrayList<>();

        if(planList.isEmpty()){
            throw new CustomException(ErrorCode.PLAN_NOT_FOUND);
        } else {
            for(Plan plan : planList) {
                placeList.add(new PlanResponseDto(plan, user.getUserId(),
                        placeRepository.findByPlanId(plan.getId()).stream().map(PlaceResponseDto::new).toList()));
            }
        }

        return placeList;
    }

    public List<PlanResponseDto> getOnePlan(Long planId, User user) {
        Plan planList = planRepository.findById(planId).orElseThrow(() ->
                new CustomException(ErrorCode.PLAN_NOT_FOUND));
        List<PlanResponseDto> placeList = new ArrayList<>();

        placeList.add(new PlanResponseDto(planList, user.getUserId(),
                placeRepository.findByPlanId(planList.getId()).stream().map(PlaceResponseDto::new).toList()));

        return placeList;
    }

    public MsgResponseDto updatePlan(Long planId, PlanRequestDto requestDto, User user) {
        Plan plan = planRepository.findById(planId).orElseThrow(() ->
            new CustomException(ErrorCode.PLAN_NOT_FOUND)
        );

        plan.update(requestDto, user);

        for(Place place : requestDto.getPlaceList()){
            place.setPlan(plan);
            if(place.getGroup_name()==null||place.getGroup_name().isEmpty()) {
                place.setGroup_name("기타");
            }
            placeRepository.save(place);
        }

        return new MsgResponseDto(HttpServletResponse.SC_OK, "여행일정 수정 성공했습니다.");
    }

    public MsgResponseDto deletePlan(Long planId, User user) {
        Plan plan = planRepository.findById(planId).orElseThrow(() ->
                new CustomException(ErrorCode.PLAN_NOT_FOUND)
        );
        planRepository.delete(plan);
        return new MsgResponseDto(HttpServletResponse.SC_OK, "여행일정 삭제 성공했습니다.");
    }


}
