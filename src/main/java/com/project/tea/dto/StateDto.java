package com.project.tea.dto;

import com.project.tea.entity.StateEntity;
import lombok.Data;

@Data
public class StateDto {
    private Long id;
    private String state;

    public static StateDto fromEntity(StateEntity entity) {
        StateDto dto = new StateDto();
        dto.setId(entity.getId());
        dto.setState(entity.getState());
        return dto;
    }

    public StateEntity toEntity() {
        StateEntity entity = new StateEntity();
        entity.setId(this.id);
        entity.setState(this.state);
        return entity;
    }
}
