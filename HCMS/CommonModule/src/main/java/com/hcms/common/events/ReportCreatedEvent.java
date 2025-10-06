package com.hcms.common.events;

import com.hcms.common.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportCreatedEvent {
    private Long reportId;
    private Long appointmentId;
    private Long patientId;
    private ReportType type;
}
