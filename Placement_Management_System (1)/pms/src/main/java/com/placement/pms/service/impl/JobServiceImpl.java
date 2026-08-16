package com.placement.pms.service.impl;

import com.placement.pms.dto.JobDTO;
import com.placement.pms.entity.Company;
import com.placement.pms.entity.Job;
import com.placement.pms.exception.ResourceNotFoundException;
import com.placement.pms.repository.CompanyRepository;
import com.placement.pms.repository.JobRepository;
import com.placement.pms.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    @Override
    public JobDTO createJob(JobDTO dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", dto.getCompanyId()));

        Job job = Job.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .eligibleCgpa(dto.getEligibleCgpa())
                .requiredSkills(dto.getRequiredSkills())
                .packageOffered(dto.getPackageOffered())
                .applicationDeadline(dto.getApplicationDeadline())
                .active(true)
                .company(company)
                .build();

        return toDTO(jobRepository.save(job));
    }

    @Override
    @Transactional(readOnly = true)
    public JobDTO getJobById(Long id) {
        return toDTO(findJobOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobDTO> getAllJobs(Boolean activeOnly, Long companyId, Pageable pageable) {
        Page<Job> page;
        if (companyId != null) {
            page = jobRepository.findByCompanyId(companyId, pageable);
        } else if (Boolean.TRUE.equals(activeOnly)) {
            page = jobRepository.findByActiveTrue(pageable);
        } else {
            page = jobRepository.findAll(pageable);
        }
        return page.map(this::toDTO);
    }

    @Override
    public JobDTO updateJob(Long id, JobDTO dto) {
        Job job = findJobOrThrow(id);

        if (dto.getTitle() != null) job.setTitle(dto.getTitle());
        if (dto.getDescription() != null) job.setDescription(dto.getDescription());
        if (dto.getEligibleCgpa() != null) job.setEligibleCgpa(dto.getEligibleCgpa());
        if (dto.getRequiredSkills() != null) job.setRequiredSkills(dto.getRequiredSkills());
        if (dto.getPackageOffered() != null) job.setPackageOffered(dto.getPackageOffered());
        if (dto.getApplicationDeadline() != null) job.setApplicationDeadline(dto.getApplicationDeadline());

        if (dto.getCompanyId() != null && !dto.getCompanyId().equals(job.getCompany().getId())) {
            Company company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", dto.getCompanyId()));
            job.setCompany(company);
        }

        return toDTO(jobRepository.save(job));
    }

    @Override
    public void deactivateJob(Long id) {
        Job job = findJobOrThrow(id);
        job.setActive(false);
        jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) {
        Job job = findJobOrThrow(id);
        jobRepository.delete(job);
    }

    private Job findJobOrThrow(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
    }

    private JobDTO toDTO(Job job) {
        return JobDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .eligibleCgpa(job.getEligibleCgpa())
                .requiredSkills(job.getRequiredSkills())
                .packageOffered(job.getPackageOffered())
                .applicationDeadline(job.getApplicationDeadline())
                .active(job.isActive())
                .companyId(job.getCompany().getId())
                .companyName(job.getCompany().getName())
                .build();
    }
}
