package com.placement.pms.service.impl;

import com.placement.pms.dto.CompanyDTO;
import com.placement.pms.entity.Company;
import com.placement.pms.exception.DuplicateResourceException;
import com.placement.pms.exception.ResourceNotFoundException;
import com.placement.pms.repository.CompanyRepository;
import com.placement.pms.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public CompanyDTO createCompany(CompanyDTO dto) {
        if (companyRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new DuplicateResourceException("A company already exists with name: " + dto.getName());
        }
        Company company = Company.builder()
                .name(dto.getName())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .address(dto.getAddress())
                .active(true)
                .build();
        return toDTO(companyRepository.save(company));
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDTO getCompanyById(Long id) {
        return toDTO(findCompanyOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyDTO> getAllCompanies(String name, Pageable pageable) {
        Page<Company> page = (name == null || name.isBlank())
                ? companyRepository.findAll(pageable)
                : companyRepository.findByNameContainingIgnoreCase(name, pageable);
        return page.map(this::toDTO);
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO dto) {
        Company company = findCompanyOrThrow(id);
        if (dto.getName() != null) company.setName(dto.getName());
        if (dto.getContactEmail() != null) company.setContactEmail(dto.getContactEmail());
        if (dto.getContactPhone() != null) company.setContactPhone(dto.getContactPhone());
        if (dto.getAddress() != null) company.setAddress(dto.getAddress());
        company.setActive(dto.isActive());
        return toDTO(companyRepository.save(company));
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = findCompanyOrThrow(id);
        companyRepository.delete(company);
    }

    private Company findCompanyOrThrow(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));
    }

    private CompanyDTO toDTO(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .contactEmail(company.getContactEmail())
                .contactPhone(company.getContactPhone())
                .address(company.getAddress())
                .active(company.isActive())
                .build();
    }
}
