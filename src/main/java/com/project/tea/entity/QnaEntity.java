package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "qna")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QnaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 200)
    private String title;

    // 긴 텍스트: MySQL TEXT 고정
    @Column(columnDefinition = "TEXT",nullable = false)
    private String post;

    @Column(columnDefinition = "TEXT")
    private String answer;

    /** 답변이 등록/수정된 시각 */
    @CreationTimestamp
    @Column(name = "created_at",  updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private UserEntity user;
}