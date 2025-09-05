package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "choose")
public class ChooseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "t_id", referencedColumnName = "id")
    private TeaEntity tea;

    private Long count = 0L; // 선택 횟수, 기본값 0
}
