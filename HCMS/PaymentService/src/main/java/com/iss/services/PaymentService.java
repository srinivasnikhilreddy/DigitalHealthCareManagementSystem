package com.iss.services;

import com.hcms.common.enums.PaymentStatus;
import com.hcms.common.events.PaymentCompletedEvent;
import com.hcms.common.events.PaymentFailedEvent;
import com.iss.models.Payment;
import com.iss.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService
{
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final double REQUIRED_AMOUNT = 500.0;

    public Payment processPayment(Long appointmentId, Double amount) {
        if(amount < REQUIRED_AMOUNT){
            PaymentFailedEvent failed = PaymentFailedEvent.builder()
                    .appointmentId(appointmentId)
                    .reason("Insufficient amount: required ₹" + REQUIRED_AMOUNT)
                    .build();
            kafkaTemplate.send("PaymentFailed", failed);
            throw new RuntimeException("Insufficient amount");
        }

        Payment payment = Payment.builder()
                .appointmentId(appointmentId)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .build();

        return paymentRepository.save(payment);
    }

    public Payment markPaymentSuccess(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                .appointmentId(payment.getAppointmentId())
                .paymentId(paymentId)
                .amount(payment.getAmount())
                .status(PaymentStatus.SUCCESS)
                .build();

        kafkaTemplate.send("PaymentCompleted", event);
        return payment;
    }

    public Payment markPaymentFailure(Long paymentId, String reason)
    {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        PaymentFailedEvent event = PaymentFailedEvent.builder()
                .appointmentId(payment.getAppointmentId())
                .reason(reason)
                .build();

        kafkaTemplate.send("PaymentFailed", event);
        return payment;
    }

    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        payment.setStatus(PaymentStatus.REFUNDED);
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments()
    {
        return paymentRepository.findAll();
    }
}
