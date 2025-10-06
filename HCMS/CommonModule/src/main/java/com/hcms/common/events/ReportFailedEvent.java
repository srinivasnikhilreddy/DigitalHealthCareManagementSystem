package com.hcms.common.events;

import com.hcms.common.enums.ReportStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportFailedEvent
{
    private Long reportId;
    private Long appointmentId;
    private ReportStatus status; // FAILED
    private String reason;       // optional failure reason
}
