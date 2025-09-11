package com.project.tea.service;

import com.project.tea.dto.QnaDto;
import com.project.tea.entity.QnaEntity;
import com.project.tea.entity.UserEntity;
import com.project.tea.repository.QnaRepository;
import com.project.tea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<QnaDto> list(int page, int size) {
        Page<QnaEntity> p = qnaRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        return p.map(QnaDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public QnaDto get(Long id) {
        return qnaRepository.findById(id)
                .map(QnaDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
    }

    @Transactional
    public Long create(String title, String post) {
        Long userId = userService.getCurrentUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow();

        QnaEntity e = QnaEntity.builder()
                .title(title)
                .post(post)
                .answer(null)
                .user(user)
                .build();
        qnaRepository.save(e);
        return e.getId();
    }

}
