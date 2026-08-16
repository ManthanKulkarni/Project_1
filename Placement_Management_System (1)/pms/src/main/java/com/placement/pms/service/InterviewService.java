package com.placement.pms.service;

import com.placement.pms.dto.InterviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewService {
    InterviewDTO scheduleInterview(InterviewDTO dto);
    InterviewDTO getInterviewById(Long id);
    Page<InterviewDTO> getInterviewsByApplication(Long applicationId, Pageable pageable);
    InterviewDTO updateInterview(Long id, InterviewDTO dto);
}
