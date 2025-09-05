package com.project.tea.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="user_data")
public class UserDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "u_id", nullable = false)
    private UserEntity user;
    // 여러 개의 엔티티가 하나의 엔티티에 연결될 때 사용합니다.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // 명시적으로 FK 컬럼명을 맞추기 위해서 사용합니다.
    @JoinColumn(name = "t_id", nullable = false)
    private TeaEntity tea;

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    // 대용량 데이터를 저장하는 어노테이션 입니다.
    @Lob
    private String memo;

    @Column(name = "update_date")
    private LocalDateTime updateDate;


}
