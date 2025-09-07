package com.project.tea.repository;

import com.project.tea.entity.MoodCheckEntity;
import com.project.tea.entity.MoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoodCheckRepository extends JpaRepository<MoodCheckEntity, Long> {

    List<MoodCheckEntity> findAll();
}
