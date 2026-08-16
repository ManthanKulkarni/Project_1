package com.placement.pms.repository;

import com.placement.pms.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Page<Interview> findByApplicationId(Long applicationId, Pageable pageable);
}
