package com.placement.pms.repository;

import com.placement.pms.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByNameIgnoreCase(String name);
    Page<Company> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
