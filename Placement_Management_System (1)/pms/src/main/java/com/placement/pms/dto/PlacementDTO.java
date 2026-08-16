package com.placement.pms.dto;

import com.placement.pms.entity.PlacementStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementDTO {
    private Long id;

    @NotNull(message = "Student id is required")
    private Long studentId;

    private String studentName;

    @NotNull(message = "Company id is required")
    private Long companyId;

    private String companyName;

    @NotNull(message = "Job id is required")
    private Long jobId;

    private String jobTitle;

    private Double packageOffered;

    private LocalDate joiningDate;

    private PlacementStatus status;
}
