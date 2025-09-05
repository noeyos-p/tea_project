package com.project.tea.repository;

import com.project.tea.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository
 * - UserEntity 관련 DB 접근 담당
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * 이메일로 유저 조회
     * @param email 로그인한 유저 이메일
     * @return Optional<UserEntity>
     */
    Optional<UserEntity> findByEmail(String email);
}
