package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "qna")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String post;

    @Column(columnDefinition = "TEXT")
    private String answer;

    //FK: user(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private UserEntity user;
}
