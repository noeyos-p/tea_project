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
    private Long moodId;
    private Long stateId;

    private LocalDate date;
    private String memo;
    private LocalDateTime updateDate;

    // Entity → DTO 변환
    public static UserDataDto fromEntity(UserDataEntity e) {
        if (e == null) return null;

        UserDataDto dto = new UserDataDto();
        dto.setId(e.getId());
        dto.setDate(e.getDate());
        dto.setMemo(e.getMemo());
        dto.setUpdateDate(e.getUpdateDate());

        if (e.getUser() != null) {
            dto.setUserId(e.getUser().getId());
        }

        if (e.getTea() != null) dto.setTeaId(e.getTea().getId());
        if (e.getMood() != null) dto.setMoodId(e.getMood().getId());
        if (e.getState() != null) dto.setStateId(e.getState().getId());

        return dto;
    }

    // DTO → Entity 변환
    public UserDataEntity toEntity() {
        UserDataEntity e = new UserDataEntity();
        e.setDate(this.date);
        e.setMemo(this.memo);
        e.setUpdateDate(this.updateDate);

        if (this.userId != null) {
            UserEntity user = new UserEntity();
            user.setId(this.userId);
            e.setUser(user);
        }

        if (this.teaId != null) {
            TeaEntity tea = new TeaEntity();
            tea.setId(this.teaId);
            e.setTea(tea);
        }

        if (this.moodId != null) {
            MoodEntity mood = new MoodEntity();
            mood.setId(this.moodId);
            e.setMood(mood);
        }

        if (this.stateId != null) {
            StateEntity state = new StateEntity();
            state.setId(this.stateId);
            e.setState(state);
        }

        return e;
    }
}
