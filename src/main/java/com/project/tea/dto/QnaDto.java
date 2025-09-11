package com.project.tea.dto;

import com.project.tea.entity.QnaEntity;
import com.project.tea.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaDto {

    private Long id;
    private String title;
    private String post;
    private String answer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime answeredAt;

    // 연관: FK만 노출
    private Long userId;
    private String userNickname; // 편의 정보
    private String userEmail;    // 편의 정보

    public static QnaDto fromEntity(QnaEntity e) {
        if (e == null) return null;
        return QnaDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .post(e.getPost())
                .answer(e.getAnswer())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .answeredAt(e.getAnsweredAt())
                .userId(e.getUser() != null ? e.getUser().getId() : null)
                .userNickname(e.getUser() != null ? e.getUser().getNickname() : null)
                .userEmail(e.getUser() != null ? e.getUser().getEmail() : null)
                .build();
    }

    public QnaEntity toEntity() {
        return QnaEntity.builder()
                .id(this.id)
                .title(this.title)
                .post(this.post)
                .answer(this.answer)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .answeredAt(this.answeredAt)
                .user(this.userId != null ? UserEntity.builder().id(this.userId).build() : null)
                .build();
    }
}
