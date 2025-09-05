package com.project.tea.repository;

import com.project.tea.entity.TeaEntity;
import com.project.tea.entity.MoodEntity;
import com.project.tea.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeaRepository extends JpaRepository<TeaEntity, Long> {
    List<TeaEntity> findByMood(MoodEntity mood);
    List<TeaEntity> findByState(StateEntity state);
}
