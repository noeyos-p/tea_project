package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //FK: user(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private UserEntity user;

    //FK: tea(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "t_id")
    private TeaEntity tea;

    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String memo;

    private LocalDateTime updateDate;
}
