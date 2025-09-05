package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.swing.undo.StateEdit;

@Entity
@Table(name = "tea")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String eat;

    @Column(columnDefinition = "TEXT")
    private String caution;

    //FK: state(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "s_id")
    private StateEntity entity;
}
