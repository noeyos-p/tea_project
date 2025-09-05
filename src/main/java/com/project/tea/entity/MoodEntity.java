package com.project.tea.entity;

import jakarta.persistence.*;

@Entity
public class MoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String mood;

    //FK: tea(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "t_id")
    private TeaEntity tea;
}
