package com.project.tea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "choose")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChooseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // FK: tea(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "t_id")
    private TeaEntity tea;

    private Integer count;
}
