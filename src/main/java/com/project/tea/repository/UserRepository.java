package com.project.tea.repository;

import com.project.tea.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 이메일로 사용자 조회 (로그인/현재 유저 식별에 사용)
    Optional<UserEntity> findByEmail(String email);

    // 닉네임 중복/조회 대응
    Optional<UserEntity> findByNickname(String nickname);

    // 닉네임 중복 체크 (닉네임 변경/회원가입 시 사용)
    boolean existsByNickname(String nickname);

    // 선택: 이메일 중복 체크가 필요하면 사용 (회원가입 시 편리)
    boolean existsByEmail(String email);
}