package com.project.tea.repository;

import com.project.tea.entity.UserDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserDataRepository
 * - 유저가 저장한 티/체크리스트 기록 관리
 */
@Repository
public interface UserDataRepository extends JpaRepository<UserDataEntity, Long> {
    // 필요 시 사용자별 기록 조회 메서드 추가 가능
}
