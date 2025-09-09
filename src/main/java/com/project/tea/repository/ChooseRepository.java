package com.project.tea.repository;

import com.project.tea.entity.ChooseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChooseRepository extends JpaRepository<ChooseEntity, Long> {

    // 집계수 내림차순 TOP N
    List<ChooseEntity> findAllByOrderByCountDesc(Pageable pageable);

    // 카운트 +1
    @Modifying
    @Query("update ChooseEntity c set c.count = coalesce(c.count,0) + 1 where c.tea.id = :teaId")
    int incrementCount(@Param("teaId") Long teaId);

    // 단일 차의 누적 선택 수 조회
    Optional<ChooseEntity> findByTea_Id(Long teaId);
}