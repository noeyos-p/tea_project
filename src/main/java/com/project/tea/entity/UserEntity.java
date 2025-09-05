package com.project.tea.entity;

import jakarta.persistence.*;

@Entity

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // email이 null 이면 안되고, 중복없이 처리하기 위해 사용합니다.
    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

}


