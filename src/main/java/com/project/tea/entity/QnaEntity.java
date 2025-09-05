package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "qna")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QnaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String post;
    @Column(columnDefinition = "TEXT")
    private String answer;

    // 여러 개의 엔티티가 하나의 엔티티에 연결될 때 사용합니다.
    @ManyToOne
    // 명시적으로 FK 컬럼명을 맞추기 위해서 사용합니다.
    @JoinColumn(name = "u_id")
    private UserEntity user;
}
