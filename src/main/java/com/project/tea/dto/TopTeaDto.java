package com.project.tea.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TopTeaDto {
    private int rank;
    private Long teaId;
    private String name;
    private String imageUrl; // 테스트: 모두 /img/background.jpg
}
