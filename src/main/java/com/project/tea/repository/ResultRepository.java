package com.project.tea.repository;

import com.project.tea.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Long> {

    // Mood별 랜덤 1개 메시지
    @Query(value = "SELECT * FROM result WHERE m_id = :moodId ORDER BY RAND() LIMIT 1", nativeQuery = true)
    ResultEntity findRandomByMood(@Param("moodId") Long moodId);

    // State별 랜덤 1개 메시지
    @Query(value = "SELECT * FROM result WHERE s_id = :stateId ORDER BY RAND() LIMIT 1", nativeQuery = true)
    ResultEntity findRandomByState(@Param("stateId") Long stateId);
}
