package com.project.tea.dto;

import com.project.tea.entity.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDataDto {
    private Long id;

    private Long userId;
    private Long teaId;

    private LocalDate date;
    private String memo;
    private LocalDateTime updateDate;

    public static UserDataDto fromEntity(UserDataEntity e) {
        if (e == null) return null;
        UserDataDto dto = new UserDataDto();
        dto.setId(e.getId());
        dto.setDate(e.getDate());
        dto.setMemo(e.getMemo());
        dto.setUpdateDate(e.getUpdateDate());
        if (e.getUser() != null) dto.setUserId(e.getUser().getId());
        if (e.getTea()  != null) dto.setTeaId(e.getTea().getId());
        return dto;
    }

    public UserDataEntity toEntity() {
        UserDataEntity e = new UserDataEntity();
        e.setDate(this.date);
        e.setMemo(this.memo);
        e.setUpdateDate(this.updateDate);

        if (this.userId != null) {

            e.setUser(UserEntity.builder().id(this.userId).build());
        }
        if (this.teaId != null) {
            TeaEntity t = new TeaEntity();
            t.setId(this.teaId);
            e.setTea(t);
        }
        return e;
    }
}
