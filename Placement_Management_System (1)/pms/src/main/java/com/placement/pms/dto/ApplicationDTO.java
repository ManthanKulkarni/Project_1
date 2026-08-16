package com.placement.pms.dto;

import com.placement.pms.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {
    private Long id;

    @NotNull(message = "Student id is required")
    private Long studentId;

    private String studentName;

    @NotNull(message = "Job id is required")
    private Long jobId;

    private String jobTitle;

    private String companyName;

    private ApplicationStatus status;

    private LocalDateTime appliedDate;
}
