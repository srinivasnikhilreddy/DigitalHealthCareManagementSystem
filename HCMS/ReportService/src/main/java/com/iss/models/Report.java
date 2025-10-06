package com.iss.models;

import com.hcms.common.enums.ReportStatus;
import com.hcms.common.enums.ReportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Correct foreign key relation
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    private ReportType type; // PRESCRIPTION, LAB

    @Enumerated(EnumType.STRING)
    private ReportStatus status; // GENERATED, DELIVERED, FAILED

    @Size(max = 2000)
    private String reportData; // optional
}
