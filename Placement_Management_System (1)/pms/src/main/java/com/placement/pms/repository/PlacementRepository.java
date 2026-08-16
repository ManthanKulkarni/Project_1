package com.placement.pms.repository;

import com.placement.pms.entity.Placement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlacementRepository extends JpaRepository<Placement, Long> {
    Optional<Placement> findByStudentId(Long studentId);
    boolean existsByStudentId(Long studentId);
    Page<Placement> findByCompanyId(Long companyId, Pageable pageable);
}
