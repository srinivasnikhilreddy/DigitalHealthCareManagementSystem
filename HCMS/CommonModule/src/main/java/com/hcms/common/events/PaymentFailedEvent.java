package com.hcms.common.events;

import com.hcms.common.enums.PaymentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFailedEvent {
    private Long paymentId;
    private Long appointmentId;
    private PaymentStatus status; // FAILED
    private String reason;        // optional failure reason
}
