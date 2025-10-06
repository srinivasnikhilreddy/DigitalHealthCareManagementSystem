package com.iss.models;

import com.hcms.common.enums.AppointmentSlot;
import com.hcms.common.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private String patientName;

    @NotNull
    private LocalDateTime patientDob;

    @NotNull
    private LocalDateTime requestDate; // when patient requested the appointment

    @NotNull
    private LocalDateTime appointmentDate; // scheduled appointment date/time

    @NotNull
    private String type; // e.g., "Consultation", "Follow-up"

    private Double amount;

    @NotNull
    private AppointmentSlot slot; // select time slot

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // SCHEDULED, COMPLETED, CANCELLED
}
