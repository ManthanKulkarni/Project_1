package com.placement.pms.service;

import com.placement.pms.dto.JobDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {
    JobDTO createJob(JobDTO dto);
    JobDTO getJobById(Long id);
    Page<JobDTO> getAllJobs(Boolean activeOnly, Long companyId, Pageable pageable);
    JobDTO updateJob(Long id, JobDTO dto);
    void deactivateJob(Long id);
    void deleteJob(Long id);
}
