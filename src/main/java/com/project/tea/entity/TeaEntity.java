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

    //  추가 : 정적 파일명 (예 "green.jpg")
//    @Column(name = "image_path")
//    private String imagePath;

}
