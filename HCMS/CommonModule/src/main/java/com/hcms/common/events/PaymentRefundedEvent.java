package com.hcms.common.events;

import com.hcms.common.enums.PaymentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRefundedEvent
{
    private Long paymentId;
    private Long appointmentId;
    private double amount;
    private PaymentStatus status; // REFUNDED
}
