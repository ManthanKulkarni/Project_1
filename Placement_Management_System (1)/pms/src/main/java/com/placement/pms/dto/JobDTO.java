package com.placement.pms.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {
    private Long id;

    @NotBlank(message = "Job title must not be empty")
    private String title;

    private String description;

    @DecimalMin(value = "0.0", message = "Eligible CGPA cannot be negative")
    private Double eligibleCgpa;

    private String requiredSkills;

    @PositiveOrZero(message = "Package must not be negative")
    private Double packageOffered;

    private LocalDate applicationDeadline;

    private boolean active;

    @NotNull(message = "Company id is required")
    private Long companyId;

    private String companyName;
}
