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
    private Long t_id;
    private Long count;
}
