package com.project.tea.dto;

import com.project.tea.entity.QnaEntity;
import com.project.tea.entity.UserEntity;
import lombok.Data;

@Data
public class QnaDto {
    private Long id;
    private String title;
    private String post;
    private String answer;

    // 연관: FK만 노출
    private Long userId;
    private String userNickname; // 편의 정보
    private String userEmail;    // 편의 정보

    public static QnaDto fromEntity(QnaEntity e) {
        if (e == null) return null;
        QnaDto dto = new QnaDto();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setPost(e.getPost());
        dto.setAnswer(e.getAnswer());
        if (e.getUser() != null) {
            dto.setUserId(e.getUser().getId());
            dto.setUserNickname(e.getUser().getNickname());
            dto.setUserEmail(e.getUser().getEmail());
        }
        return dto;
    }

    public QnaEntity toEntity() {
        QnaEntity e = new QnaEntity();

        e.setTitle(this.title);
        e.setPost(this.post);
        e.setAnswer(this.answer);

        if (this.userId != null) {

            e.setUser(UserEntity.builder().id(this.userId).build());
        }
        return e;
    }
}
