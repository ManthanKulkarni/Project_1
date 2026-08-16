package com.placement.pms.controller;

import com.placement.pms.dto.InterviewDTO;
import com.placement.pms.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<InterviewDTO> scheduleInterview(@Valid @RequestBody InterviewDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(interviewService.scheduleInterview(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewDTO> getInterview(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.getInterviewById(id));
    }

    @GetMapping
    public ResponseEntity<Page<InterviewDTO>> getInterviewsByApplication(
            @RequestParam Long applicationId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(interviewService.getInterviewsByApplication(applicationId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewDTO> updateInterview(@PathVariable Long id, @Valid @RequestBody InterviewDTO dto) {
        return ResponseEntity.ok(interviewService.updateInterview(id, dto));
    }
}
