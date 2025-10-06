package com.iss.services;

import com.hcms.common.events.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListenerService
{
    private final NotificationService notificationService;

    public NotificationListenerService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ---------------- Appointment Notifications ----------------
    @KafkaListener(topics = "AppointmentCreated", groupId = "NotificationGroup")
    public void onAppointmentCreated(AppointmentCreatedEvent event) {
        notificationService.sendNotification(
                event.getPatientId(),
                "EMAIL",
                "Your appointment is scheduled with doctor ID " + event.getDoctorId()
        );
    }

    @KafkaListener(topics = "AppointmentCancelled", groupId = "NotificationGroup")
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        notificationService.sendNotification(
                event.getPatientId(),
                "EMAIL",
                "Your appointment has been cancelled."
        );
    }

    @KafkaListener(topics = "AppointmentCompleted", groupId = "NotificationGroup")
    public void onAppointmentCompleted(AppointmentCompletedEvent event) {
        notificationService.sendNotification(
                event.getPatientId(),
                "EMAIL",
                "Your appointment has been marked as completed."
        );
    }

    // ---------------- Payment Notifications ----------------
    @KafkaListener(topics = "PaymentCompleted", groupId = "NotificationGroup")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        notificationService.sendNotification(
                event.getAppointmentId(), // or lookup patient by appointment
                "EMAIL",
                "Payment of ₹" + event.getAmount() + " was successful for your appointment."
        );
    }

    @KafkaListener(topics = "PaymentFailed", groupId = "NotificationGroup")
    public void onPaymentFailed(PaymentFailedEvent event) {
        notificationService.sendNotification(
                event.getAppointmentId(), // or lookup patient by appointment
                "EMAIL",
                "Payment failed for your appointment. Reason: " + event.getReason()
        );
    }

    @KafkaListener(topics = "PaymentRefunded", groupId = "NotificationGroup")
    public void onPaymentRefunded(PaymentRefundedEvent event) {
        notificationService.sendNotification(
                event.getAppointmentId(),
                "EMAIL",
                "Your payment of ₹" + event.getAmount() + " has been refunded."
        );
    }

    // ---------------- Report Notifications ----------------
    @KafkaListener(topics = "ReportGenerated", groupId = "NotificationGroup")
    public void onReportGenerated(ReportGeneratedEvent event)
    {
        System.out.println("yes rey report generated "+event);
        notificationService.sendNotification(
                event.getAppointmentId(),
                "EMAIL",
                "A new medical report has been created for your appointment."
        );
    }

    @KafkaListener(topics = "ReportCreated", groupId = "NotificationGroup")
    public void onReportCreated(ReportCreatedEvent event)
    {
        System.out.println("yes rey report created "+event);
        notificationService.sendNotification(
                event.getPatientId(),
                "EMAIL",
                "A new medical report has been created for your appointment."
        );
    }

    @KafkaListener(topics = "ReportDelivered", groupId = "NotificationGroup")
    public void onReportDelivered(ReportDeliveredEvent event) {
        notificationService.sendNotification(
                event.getPatientId(),
                "EMAIL",
                "Your medical report has been delivered."
        );
    }

    // ---------------- User Notifications ----------------
    @KafkaListener(topics = "PatientRegistered", groupId = "NotificationGroup")
    public void onPatientRegistered(PatientRegisteredEvent event) {
        notificationService.sendNotification(
                event.getPatientId(),
                "EMAIL",
                "Welcome " + event.getPatientName() + "! Your patient account has been created successfully."
        );
    }

    @KafkaListener(topics = "DoctorRegistered", groupId = "NotificationGroup")
    public void onDoctorRegistered(DoctorRegisteredEvent event) {
        notificationService.sendNotification(
                event.getDoctorId(),
                "EMAIL",
                "Welcome Dr. " + event.getDoctorName() + "! Your doctor profile has been registered successfully."
        );
    }
}
