package com.sparta.travel.domain.entity;

import com.sparta.travel.domain.dto.PlanRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "plan")
@Getter
@Setter
@NoArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name ="city", nullable = false)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "plan", cascade = {CascadeType.REMOVE})
    private List<Place> placeList = new ArrayList<>();

    public Plan(PlanRequestDto requestDto, User user) {
        this.date = requestDto.getDate();
        this.city = requestDto.getCity();
        this.user = user;
        this.placeList = requestDto.getPlaceList();
    }

    public void update(PlanRequestDto requestDto, User user) {
        this.date = requestDto.getDate();
        this.city = requestDto.getCity();
        this.user = user;
        this.placeList = requestDto.getPlaceList();
    }
}
