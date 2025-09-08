package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "result")
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String result;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private MoodEntity mood;

    @ManyToOne
    @JoinColumn(name = "s_id")
    private StateEntity state;
}
