package com.placement.pms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    private Integer round;

    private LocalDateTime scheduledDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InterviewResult result = InterviewResult.PENDING;

    @Column(length = 1000)
    private String feedback;
}
