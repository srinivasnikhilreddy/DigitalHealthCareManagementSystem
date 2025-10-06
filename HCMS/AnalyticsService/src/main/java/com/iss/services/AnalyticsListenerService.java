package com.iss.services;

import com.hcms.common.events.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AnalyticsListenerService
{
    private final AtomicInteger totalAppointments = new AtomicInteger(0);
    private final AtomicInteger cancelledAppointments = new AtomicInteger(0);
    private final AtomicInteger completedAppointments = new AtomicInteger(0);

    private final AtomicLong totalRevenue = new AtomicLong(0);
    private final AtomicInteger failedPayments = new AtomicInteger(0);
    private final AtomicInteger refundedPayments = new AtomicInteger(0);

    private final AtomicInteger totalReports = new AtomicInteger(0);
    private final AtomicInteger deliveredReports = new AtomicInteger(0);

    private final AtomicInteger totalPatients = new AtomicInteger(0);
    private final AtomicInteger totalDoctors = new AtomicInteger(0);

    @KafkaListener(topics = "AppointmentCreated", groupId = "AnalyticsGroup")
    public void onAppointmentCreated(AppointmentCreatedEvent event)
    {
        totalAppointments.incrementAndGet();
        System.out.println("Analytics: Appointment created. Total = " + totalAppointments.get());
    }

    @KafkaListener(topics = "AppointmentCancelled", groupId = "AnalyticsGroup")
    public void onAppointmentCancelled(AppointmentCancelledEvent event)
    {
        cancelledAppointments.incrementAndGet();
        System.out.println("Analytics: Appointment cancelled. Cancelled count = " + cancelledAppointments.get());
    }

    @KafkaListener(topics = "AppointmentCompleted", groupId = "AnalyticsGroup")
    public void onAppointmentCompleted(AppointmentCompletedEvent event)
    {
        completedAppointments.incrementAndGet();
        System.out.println("Analytics: Appointment completed. Completed count = " + completedAppointments.get());
    }

    @KafkaListener(topics = "PaymentCompleted", groupId = "AnalyticsGroup")
    public void onPaymentCompleted(PaymentCompletedEvent event)
    {
        totalRevenue.addAndGet((long) event.getAmount());
        System.out.println("Analytics: Payment success. Total revenue = " + totalRevenue.get());
    }

    @KafkaListener(topics = "PaymentFailed", groupId = "AnalyticsGroup")
    public void onPaymentFailed(PaymentFailedEvent event)
    {
        failedPayments.incrementAndGet();
        System.out.println("Analytics: Payment failed. Failed count = " + failedPayments.get());
    }

    @KafkaListener(topics = "PaymentRefunded", groupId = "AnalyticsGroup")
    public void onPaymentRefunded(PaymentRefundedEvent event)
    {
        refundedPayments.incrementAndGet();
        totalRevenue.addAndGet(-(long) event.getAmount());
        System.out.println("Analytics: Payment refunded. Refunded count = " + refundedPayments.get() +
                " | Total revenue = " + totalRevenue.get());
    }

    @KafkaListener(topics = "ReportCreated", groupId = "AnalyticsGroup")
    public void onReportCreated(ReportCreatedEvent event)
    {
        totalReports.incrementAndGet();
        System.out.println("Analytics: Report created. Total reports = " + totalReports.get());
    }

    @KafkaListener(topics = "ReportDelivered", groupId = "AnalyticsGroup")
    public void onReportDelivered(ReportDeliveredEvent event)
    {
        deliveredReports.incrementAndGet();
        System.out.println("Analytics: Report delivered. Delivered reports = " + deliveredReports.get());
    }

    @KafkaListener(topics = "PatientRegistered", groupId = "AnalyticsGroup")
    public void onPatientRegistered(PatientRegisteredEvent event)
    {
        totalPatients.incrementAndGet();
        System.out.println("Analytics: Patient registered. Total patients = " + totalPatients.get());
    }

    @KafkaListener(topics = "DoctorRegistered", groupId = "AnalyticsGroup")
    public void onDoctorRegistered(DoctorRegisteredEvent event)
    {
        totalDoctors.incrementAndGet();
        System.out.println("Analytics: Doctor registered. Total doctors = " + totalDoctors.get());
    }
}
