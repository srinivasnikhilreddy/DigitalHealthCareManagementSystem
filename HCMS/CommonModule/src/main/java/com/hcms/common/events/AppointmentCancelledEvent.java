package com.hcms.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCancelledEvent {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private String reason;
}
