package com.project.tea.repository;

import com.project.tea.entity.UserDataEntity;
import com.project.tea.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserDataEntity, Long> {
    //전체기록 조회
    List<UserDataEntity> findByUserId(Long userId);

//    Optional<UserEntity> findByEmail(String email);
}
