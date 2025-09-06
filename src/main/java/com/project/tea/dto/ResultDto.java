package com.project.tea.dto;

import com.project.tea.entity.TeaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
public class ResultDto {
    private String message;
    private List<TeaDto> teas;
    private Long resultId;

    // 기존: Entity -> DTO 변환
    public static ResultDto from(String message, List<TeaEntity> teas, Long resultId) {
        List<TeaDto> teaDto = teas.stream()
                .map(TeaDto::fromEntity)
                .collect(Collectors.toList());
        return new ResultDto(message, teaDto, resultId);
    }

    // 이미 DTO를 가지고 있을 때
    public static ResultDto fromDto(String message, List<TeaDto> teas, Long resultId) {
        return new ResultDto(message, teas, resultId);
    }
}
