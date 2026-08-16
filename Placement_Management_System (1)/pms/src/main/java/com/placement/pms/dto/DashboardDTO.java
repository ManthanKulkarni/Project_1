package com.placement.pms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    private long totalStudents;
    private long totalCompanies;
    private long activeJobs;
    private long totalApplications;
    private long shortlistedStudents;
    private long selectedStudents;
}
