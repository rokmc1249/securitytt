package com.sparta.travel.domain.entity;

import com.sparta.travel.domain.dto.BookmarkRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bookmark")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String place_name;

    @Column(nullable = false)
    private String address_name;

    @Column(nullable = false)
    private String road_address_name;

    @Column(nullable = false)
    private String x;

    @Column(nullable = false)
    private String y;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String group_name;

    @Column(nullable = false)
    private String img_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Bookmark(BookmarkRequestDto bookmarkRequestDto,User user){
        this.place_name = bookmarkRequestDto.getPlace_name();
        this.address_name = bookmarkRequestDto.getAddress_name();
        this.road_address_name = bookmarkRequestDto.getRoad_address_name();
        this.x = bookmarkRequestDto.getX();
        this.y = bookmarkRequestDto.getY();
        this.city = bookmarkRequestDto.getCity();
        this.img_url = bookmarkRequestDto.getImg_url();
        this.group_name = (bookmarkRequestDto.getGroup_name()==null||bookmarkRequestDto.getGroup_name().isEmpty())?"기타": bookmarkRequestDto.getGroup_name();
        this.user = user;
    }

}
