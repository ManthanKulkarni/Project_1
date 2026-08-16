package com.placement.pms.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDTO {
    private Long id;

    @NotBlank(message = "Company name must not be empty")
    private String name;

    @Email(message = "Contact email must be valid")
    private String contactEmail;

    private String contactPhone;

    private String address;

    private boolean active;
}
