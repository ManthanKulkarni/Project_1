package com.placement.pms.dto;

import com.placement.pms.entity.InterviewResult;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewDTO {
    private Long id;

    @NotNull(message = "Application id is required")
    private Long applicationId;

    @Positive(message = "Round must be a positive number")
    private Integer round;

    private LocalDateTime scheduledDate;

    private InterviewResult result;

    private String feedback;
}
