package com.placement.pms.service.impl;

import com.placement.pms.dto.DashboardDTO;
import com.placement.pms.entity.ApplicationStatus;
import com.placement.pms.repository.*;
import com.placement.pms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public DashboardDTO getDashboardStats() {
        return DashboardDTO.builder()
                .totalStudents(studentRepository.count())
                .totalCompanies(companyRepository.count())
                .activeJobs(jobRepository.countByActiveTrue())
                .totalApplications(applicationRepository.count())
                .shortlistedStudents(applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED))
                .selectedStudents(applicationRepository.countByStatus(ApplicationStatus.SELECTED))
                .build();
    }
}
