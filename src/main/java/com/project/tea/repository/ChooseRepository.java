package com.project.tea.repository;

import com.project.tea.entity.ChooseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChooseRepository extends JpaRepository<ChooseEntity, Long> {
}
