package com.project.tea.repository;

import com.project.tea.entity.ChooseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChooseRepository extends JpaRepository<ChooseEntity, Long> {

    /** tea 연관키로 단건 조회 */
    Optional<ChooseEntity> findByTea_Id(Long teaId);

    /** count 내림차순 TOP N (PageRequest.of(0, N)로 한정) */
    @EntityGraph(attributePaths = "tea")
    Page<ChooseEntity> findAllByOrderByCountDesc(Pageable pageable);

    /** 동시성 안전: 읽기 없이 count 를 한방에 +1 */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ChooseEntity c set c.count = c.count + 1 where c.tea.id = :teaId")
    int incrementCount(@Param("teaId") Long teaId);
}
