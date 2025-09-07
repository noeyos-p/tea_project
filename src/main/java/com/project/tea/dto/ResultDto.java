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
    private String type; // "mood" 또는 "state"

    // Entity -> DTO 변환 시
    public static ResultDto from(String message, List<TeaEntity> teas, Long resultId, String type) {
        List<TeaDto> teaDto = teas.stream()
                .map(TeaDto::fromEntity)
                .collect(Collectors.toList());
        return new ResultDto(message, teaDto, resultId, type);
    }

    // 이미 DTO를 가지고 있을 때
    public static ResultDto fromDto(String message, List<TeaDto> teas, Long resultId, String type) {
        return new ResultDto(message, teas, resultId, type);
    }
}
