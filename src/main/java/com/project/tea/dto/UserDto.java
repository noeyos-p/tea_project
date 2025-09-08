package com.project.tea.dto;

import com.project.tea.entity.UserEntity;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String nickname;

    public static UserDto fromEntity(UserEntity e) {
        if (e == null) return null;
        UserDto dto = new UserDto();
        dto.setId(e.getId());
        dto.setEmail(e.getEmail());
        dto.setNickname(e.getNickname());
        return dto;
    }

    public UserEntity toEntity() {

        return UserEntity.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }
}
