package com.placement.pms.service.impl;

import com.placement.pms.dto.ApplicationDTO;
import com.placement.pms.entity.Application;
import com.placement.pms.entity.ApplicationStatus;
import com.placement.pms.entity.Job;
import com.placement.pms.entity.Student;
import com.placement.pms.exception.ApplicationAlreadyExistsException;
import com.placement.pms.exception.InvalidRequestException;
import com.placement.pms.exception.ResourceNotFoundException;
import com.placement.pms.exception.StudentNotEligibleException;
import com.placement.pms.repository.ApplicationRepository;
import com.placement.pms.repository.JobRepository;
import com.placement.pms.repository.StudentRepository;
import com.placement.pms.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;

    @Override
    public ApplicationDTO applyForJob(ApplicationDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", dto.getStudentId()));
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", dto.getJobId()));

        if (!job.isActive()) {
            throw new InvalidRequestException("This job opening is no longer active");
        }

        if (applicationRepository.existsByStudentIdAndJobId(student.getId(), job.getId())) {
            throw new ApplicationAlreadyExistsException(
                    "Student has already applied for this job");
        }

        if (job.getEligibleCgpa() != null && student.getCgpa() != null
                && student.getCgpa() < job.getEligibleCgpa()) {
            throw new StudentNotEligibleException(
                    "Student CGPA (" + student.getCgpa() + ") is below the required eligibility ("
                            + job.getEligibleCgpa() + ") for this job");
        }

        Application application = Application.builder()
                .student(student)
                .job(job)
                .status(ApplicationStatus.APPLIED)
                .build();

        return toDTO(applicationRepository.save(application));
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationDTO getApplicationById(Long id) {
        return toDTO(findApplicationOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationDTO> getAllApplications(Long studentId, Long jobId, ApplicationStatus status, Pageable pageable) {
        Page<Application> page;
        if (studentId != null) {
            page = applicationRepository.findByStudentId(studentId, pageable);
        } else if (jobId != null) {
            page = applicationRepository.findByJobId(jobId, pageable);
        } else if (status != null) {
            page = applicationRepository.findByStatus(status, pageable);
        } else {
            page = applicationRepository.findAll(pageable);
        }
        return page.map(this::toDTO);
    }

    @Override
    public ApplicationDTO updateStatus(Long id, ApplicationStatus status) {
        Application application = findApplicationOrThrow(id);
        application.setStatus(status);
        return toDTO(applicationRepository.save(application));
    }

    @Override
    public void deleteApplication(Long id) {
        Application application = findApplicationOrThrow(id);
        applicationRepository.delete(application);
    }

    private Application findApplicationOrThrow(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));
    }

    private ApplicationDTO toDTO(Application application) {
        return ApplicationDTO.builder()
                .id(application.getId())
                .studentId(application.getStudent().getId())
                .studentName(application.getStudent().getName())
                .jobId(application.getJob().getId())
                .jobTitle(application.getJob().getTitle())
                .companyName(application.getJob().getCompany().getName())
                .status(application.getStatus())
                .appliedDate(application.getAppliedDate())
                .build();
    }
}
