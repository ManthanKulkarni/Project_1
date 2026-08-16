package com.placement.pms.service.impl;

import com.placement.pms.dto.PlacementDTO;
import com.placement.pms.entity.Company;
import com.placement.pms.entity.Job;
import com.placement.pms.entity.Placement;
import com.placement.pms.entity.Student;
import com.placement.pms.exception.DuplicateResourceException;
import com.placement.pms.exception.ResourceNotFoundException;
import com.placement.pms.repository.CompanyRepository;
import com.placement.pms.repository.JobRepository;
import com.placement.pms.repository.PlacementRepository;
import com.placement.pms.repository.StudentRepository;
import com.placement.pms.service.PlacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlacementServiceImpl implements PlacementService {

    private final PlacementRepository placementRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    @Override
    public PlacementDTO createPlacement(PlacementDTO dto) {
        if (placementRepository.existsByStudentId(dto.getStudentId())) {
            throw new DuplicateResourceException("Student already has a placement record");
        }

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", dto.getStudentId()));
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", dto.getCompanyId()));
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", dto.getJobId()));

        Placement placement = Placement.builder()
                .student(student)
                .company(company)
                .job(job)
                .packageOffered(dto.getPackageOffered())
                .joiningDate(dto.getJoiningDate())
                .status(dto.getStatus() != null ? dto.getStatus() : com.placement.pms.entity.PlacementStatus.OFFERED)
                .build();

        return toDTO(placementRepository.save(placement));
    }

    @Override
    @Transactional(readOnly = true)
    public PlacementDTO getPlacementById(Long id) {
        Placement placement = placementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement", "id", id));
        return toDTO(placement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlacementDTO> getAllPlacements(Pageable pageable) {
        return placementRepository.findAll(pageable).map(this::toDTO);
    }

    private PlacementDTO toDTO(Placement placement) {
        return PlacementDTO.builder()
                .id(placement.getId())
                .studentId(placement.getStudent().getId())
                .studentName(placement.getStudent().getName())
                .companyId(placement.getCompany().getId())
                .companyName(placement.getCompany().getName())
                .jobId(placement.getJob().getId())
                .jobTitle(placement.getJob().getTitle())
                .packageOffered(placement.getPackageOffered())
                .joiningDate(placement.getJoiningDate())
                .status(placement.getStatus())
                .build();
    }
}
