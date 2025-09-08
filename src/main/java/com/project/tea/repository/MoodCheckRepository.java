package com.project.tea.repository;

import com.project.tea.entity.MoodCheckEntity;
import com.project.tea.entity.MoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoodCheckRepository extends JpaRepository<MoodCheckEntity, Long> {

    //30개리스트 랜덤으로 보여줌
    @Query(value = "SELECT * FROM mood_check ORDER BY RAND()", nativeQuery = true)
    List<MoodCheckEntity> findAllRandomOrder();

}
