package com.project.tea.repository;

import com.project.tea.entity.MoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoodRepository extends JpaRepository<MoodEntity, Long> {
    Optional<MoodEntity> findByMood(String mood);
}
