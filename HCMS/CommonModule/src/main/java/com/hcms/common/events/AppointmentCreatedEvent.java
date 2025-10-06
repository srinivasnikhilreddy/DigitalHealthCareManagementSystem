package com.hcms.common.events;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentCreatedEvent
{
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentTime;
    private Double amount;
}
