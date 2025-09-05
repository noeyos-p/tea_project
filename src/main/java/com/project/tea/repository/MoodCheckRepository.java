package com.project.tea.repository;

import com.project.tea.entity.MoodCheckEntity;
import com.project.tea.entity.MoodEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoodCheckRepository {
    // 특정 Mood에 연결된 체크리스트 조회
    List<MoodCheckEntity> findByMood(MoodEntity mood);

    Optional<Object> findById(Long id);
}
