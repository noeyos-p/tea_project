package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mood_check")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodCheckEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String list;

    //FK: mood(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "m_id")
    private MoodEntity mood;
}
