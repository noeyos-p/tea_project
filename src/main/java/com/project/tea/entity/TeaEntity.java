package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tea")
public class TeaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String content;
    private String eat;
    private String caution;
    @ManyToOne
    @JoinColumn(name = "m_id", referencedColumnName = "id")
    private MoodEntity mood;

    @ManyToOne
    @JoinColumn(name = "s_id", referencedColumnName = "id")
    private StateEntity state;

}
