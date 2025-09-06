package com.project.tea.repository;

import com.project.tea.entity.UserDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDataRepository extends JpaRepository<UserDataEntity, Long> {
    // UserEntity의 id 기준으로 UserDataEntity 조회
    List<UserDataEntity> findByUser_Id(Long userId);
}
