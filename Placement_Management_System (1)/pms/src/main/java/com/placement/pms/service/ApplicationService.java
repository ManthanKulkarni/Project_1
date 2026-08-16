package com.placement.pms.service;

import com.placement.pms.dto.ApplicationDTO;
import com.placement.pms.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {
    ApplicationDTO applyForJob(ApplicationDTO dto);
    ApplicationDTO getApplicationById(Long id);
    Page<ApplicationDTO> getAllApplications(Long studentId, Long jobId, ApplicationStatus status, Pageable pageable);
    ApplicationDTO updateStatus(Long id, ApplicationStatus status);
    void deleteApplication(Long id);
}
