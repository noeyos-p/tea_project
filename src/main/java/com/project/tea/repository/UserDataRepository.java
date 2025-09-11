package com.project.tea.repository;

import com.project.tea.entity.UserDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserDataEntity, Long> {

    // ✅ 연관경로 기반 메소드로 수정 (userId -> user.id)
    List<UserDataEntity> findByUserId(Long userId);

    // ✅ 오늘 메모(내용 있는 것만) 최신순
    @Query("""
      SELECT u FROM UserDataEntity u
      WHERE u.user.id = :userId AND u.date = :today AND u.memo IS NOT NULL
      ORDER BY u.updateDate DESC
    """)
    List<UserDataEntity> findTodayMemos(@Param("userId") Long userId, @Param("today") LocalDate today);

    // ✅ 특정 날짜의 기록 조회
    @Query("""
      SELECT u FROM UserDataEntity u
      WHERE u.user.id = :userId AND u.date = :date
    """)
    List<UserDataEntity> findByUserIdAndDate(@Param("userId") Long userId,
                                             @Param("date") LocalDate date);

    // ✅ 하루 1개 제한을 위한 존재 여부/단건 조회
    boolean existsByUser_IdAndDate(Long userId, LocalDate date);

    UserDataEntity findTopByUser_IdAndDateOrderByUpdateDateDesc(Long userId, LocalDate date);

}
