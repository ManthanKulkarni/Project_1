package com.placement.pms.service.impl;

import com.placement.pms.dto.InterviewDTO;
import com.placement.pms.entity.Application;
import com.placement.pms.entity.Interview;
import com.placement.pms.exception.ResourceNotFoundException;
import com.placement.pms.repository.ApplicationRepository;
import com.placement.pms.repository.InterviewRepository;
import com.placement.pms.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public InterviewDTO scheduleInterview(InterviewDTO dto) {
        Application application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", dto.getApplicationId()));

        Interview interview = Interview.builder()
                .application(application)
                .round(dto.getRound())
                .scheduledDate(dto.getScheduledDate())
                .result(dto.getResult() != null ? dto.getResult() : com.placement.pms.entity.InterviewResult.PENDING)
                .feedback(dto.getFeedback())
                .build();

        return toDTO(interviewRepository.save(interview));
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewDTO getInterviewById(Long id) {
        return toDTO(findInterviewOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewDTO> getInterviewsByApplication(Long applicationId, Pageable pageable) {
        return interviewRepository.findByApplicationId(applicationId, pageable).map(this::toDTO);
    }

    @Override
    public InterviewDTO updateInterview(Long id, InterviewDTO dto) {
        Interview interview = findInterviewOrThrow(id);
        if (dto.getRound() != null) interview.setRound(dto.getRound());
        if (dto.getScheduledDate() != null) interview.setScheduledDate(dto.getScheduledDate());
        if (dto.getResult() != null) interview.setResult(dto.getResult());
        if (dto.getFeedback() != null) interview.setFeedback(dto.getFeedback());
        return toDTO(interviewRepository.save(interview));
    }

    private Interview findInterviewOrThrow(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", id));
    }

    private InterviewDTO toDTO(Interview interview) {
        return InterviewDTO.builder()
                .id(interview.getId())
                .applicationId(interview.getApplication().getId())
                .round(interview.getRound())
                .scheduledDate(interview.getScheduledDate())
                .result(interview.getResult())
                .feedback(interview.getFeedback())
                .build();
    }
}
