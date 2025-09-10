package com.project.tea.repository;

import com.project.tea.entity.UserDataEntity;
import com.project.tea.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserDataEntity, Long> {
    //전체기록 조회
    List<UserDataEntity> findByUserId(Long userId);
    //오늘 메모만 뽑는 쿼리 추가함
    @Query("SELECT u FROM UserDataEntity u " +
            "WHERE u.user.id = :userId AND u.date = :today AND u.memo IS NOT NULL " +
            "ORDER BY u.updateDate DESC")
    List<UserDataEntity> findTodayMemos(@Param("userId") Long userId, @Param("today") LocalDate today);

}
