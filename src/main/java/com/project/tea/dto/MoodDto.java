package com.project.tea.dto;

import com.project.tea.entity.MoodEntity;
import lombok.Data;

@Data
public class MoodDto {
    private Long id;
    private String mood;

    public static MoodDto fromEntity(MoodEntity entity) {
        MoodDto dto = new MoodDto();
        dto.setId(entity.getId());
        dto.setMood(entity.getMood());
        return dto;
    }

    public MoodEntity toEntity() {
        MoodEntity entity = new MoodEntity();
        entity.setId(this.id);
        entity.setMood(this.mood);
        return entity;
    }
}
