package com.project.tea.dto;

import com.project.tea.entity.TeaEntity;
import lombok.Data;

@Data
public class TeaDto {
    private Long id;
    private String name;
    private String content;
    private String eat;
    private String caution;

    public static TeaDto fromEntity(TeaEntity tea) {
        TeaDto dto = new TeaDto();
        dto.setId(tea.getId());
        dto.setName(tea.getName());
        dto.setContent(tea.getContent());
        dto.setEat(tea.getEat());
        dto.setCaution(tea.getCaution());
        return dto;
    }

    public TeaEntity toEntity() {
        TeaEntity tea = new TeaEntity();
        tea.setId(this.id);
        tea.setName(this.name);
        tea.setContent(this.content);
        tea.setEat(this.eat);
        tea.setCaution(this.caution);
        return tea;
    }
}
