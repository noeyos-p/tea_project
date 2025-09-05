package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_data")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "u_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "t_id", nullable = false)
    private TeaEntity tea;

    // 날짜만 저장
    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    @Lob
    private String memo;

    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
