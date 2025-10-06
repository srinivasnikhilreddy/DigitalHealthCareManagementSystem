package com.hcms.common.events;

import com.hcms.common.enums.ReportStatus;
import com.hcms.common.enums.ReportType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportGeneratedEvent {
    private Long reportId;
    private Long appointmentId;
    private ReportType type;
    private ReportStatus status;
    private String reportData; // optional: text or JSON
}
