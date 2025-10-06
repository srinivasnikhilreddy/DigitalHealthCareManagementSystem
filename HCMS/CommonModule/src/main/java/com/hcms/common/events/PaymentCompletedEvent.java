package com.hcms.common.events;

import com.hcms.common.enums.PaymentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCompletedEvent
{
    private Long paymentId;
    private Long appointmentId;
    private PaymentStatus status;
    private double amount;
}
