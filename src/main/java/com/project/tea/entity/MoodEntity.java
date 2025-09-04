package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "mood")
public class MoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mood;
    private Long t_id;
}
