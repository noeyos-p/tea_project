package com.project.tea.dto;

import com.project.tea.entity.ChooseEntity;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ChooseDto {

    private Long teaId;
    private String teaName;
    private Long count;

    public static ChooseDto fromEntity(ChooseEntity e) {
        return ChooseDto.builder()
                .teaId(e.getTea().getId())
                .teaName(e.getTea().getName())
                .count(e.getCount())
                .build();
    }
}
