package com.placement.pms.service;

import com.placement.pms.dto.CompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    CompanyDTO createCompany(CompanyDTO dto);
    CompanyDTO getCompanyById(Long id);
    Page<CompanyDTO> getAllCompanies(String name, Pageable pageable);
    CompanyDTO updateCompany(Long id, CompanyDTO dto);
    void deleteCompany(Long id);
}
