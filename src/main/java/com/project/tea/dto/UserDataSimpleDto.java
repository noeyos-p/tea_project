package com.project.tea.dto;

import com.project.tea.entity.UserDataEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDataSimpleDto {
    private Long id;
    private String date;   // ISO yyyy-MM-dd (문자열)
    private String teaName;
    private String memo;

    public static UserDataSimpleDto from(UserDataEntity e) {
        return UserDataSimpleDto.builder()
                .id(e.getId())
                .date(e.getDate() != null ? e.getDate().toString() : "")
                .teaName(e.getTea() != null ? e.getTea().getName() : "")
                .memo(e.getMemo())
                .build();
    }
}
