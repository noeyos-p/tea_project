package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "qna")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QnaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // 긴 텍스트: MySQL TEXT 고정
    @Column(columnDefinition = "TEXT")
    private String post;

    @Column(columnDefinition = "TEXT")
    private String answer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private UserEntity user;
}
