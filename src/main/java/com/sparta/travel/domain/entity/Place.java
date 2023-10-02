package com.sparta.travel.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "place")
@Getter
@Setter
@NoArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "placeName",nullable = false)
    private String place_name;

    @Column(name = "addressName", nullable = false)
    private String address_name;

    @Column(name = "roadAddressName", nullable = false)
    private String road_address_name;

    @Column(name = "x", nullable = false)
    private String x;

    @Column(name = "y", nullable = false)
    private String y;

    @Column(name = "img_url", nullable = false)
    private String img_url;

    @Column(name = "group_name",nullable = false)
    private String group_name;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;


}
