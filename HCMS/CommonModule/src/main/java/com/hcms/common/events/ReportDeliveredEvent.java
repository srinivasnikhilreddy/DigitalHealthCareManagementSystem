package com.hcms.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDeliveredEvent {
    private Long reportId;
    private Long appointmentId;
    private Long patientId;
    private String deliveryChannel; // e.g., EMAIL, SMS, PORTAL
}
