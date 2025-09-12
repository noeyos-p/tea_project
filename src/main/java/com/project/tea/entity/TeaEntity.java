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

    @Lob
    private String content;  // TEXT 타입으로 변경

    @Lob
    private String eat;

    @Lob
    private String caution;

    @ManyToOne
    @JoinColumn(name = "m_id", referencedColumnName = "id")
    private MoodEntity mood;

    @ManyToOne
    @JoinColumn(name = "s_id", referencedColumnName = "id")
    private StateEntity state;
}
