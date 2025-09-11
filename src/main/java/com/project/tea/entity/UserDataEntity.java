// src/main/java/com/project/tea/entity/UserDataEntity.java
package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(
        name = "user_data",
        uniqueConstraints = {
                // ✅ 같은 유저가 같은 날짜에 2개 못 쓰도록 DB 레벨에서 차단
                @UniqueConstraint(name = "uk_user_date", columnNames = {"u_id", "date"})
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "u_id", nullable = false)
    private UserEntity user;

    // 추천받은 차
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "t_id", nullable = false)
    private TeaEntity tea;

    // ✅ 날짜만 저장 (Asia/Seoul 기준, 하루 1개 제한의 키)
    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    @Lob
    private String memo;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private MoodEntity mood;

    @ManyToOne
    @JoinColumn(name = "s_id")
    private StateEntity state;

    @PrePersist
    void onCreate() {
        if (date == null) {
            date = LocalDate.now(ZoneId.of("Asia/Seoul"));
        }
        if (updateDate == null) {
            updateDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        }
    }

    @PreUpdate
    void onUpdate() {
        updateDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
