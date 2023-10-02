package com.sparta.travel.domain.entity;

import com.sparta.travel.domain.jwt.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

//    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
//    private UserRoleEnum role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(unique = true)
    private String profile_img_url;

//    public User(String userId, String password, UserRoleEnum role,String email,String nickname) {
//        this.userId = userId;
//        this.password = password;
//        this.role = role;
//        this.email = email;
//        this.nickname = nickname;
//    }

    public User(String userId, String password,String email,String nickname) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public void update(String email,String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public void updateProfileImg(String fileName) {
        this.profile_img_url = fileName;
    }

}
