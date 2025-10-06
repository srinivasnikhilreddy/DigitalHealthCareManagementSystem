package com.iss.services;

import com.hcms.common.events.*;
import com.hcms.common.enums.*;
import com.iss.models.*;
import com.iss.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService
{
    @Autowired
    private AppointmentRepository appointmentRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    public AppointmentService(KafkaTemplate<String, Object> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Appointment bookAppointment(Appointment appointment)
    {
        appointment.setStatus(AppointmentStatus.PENDING_PAYMENT);
        appointment.setAmount(500.0); // example fee, can come from doctor profile
        Appointment saved = appointmentRepository.save(appointment);

        AppointmentCreatedEvent event = AppointmentCreatedEvent.builder()
                .appointmentId(saved.getId())
                .doctorId(saved.getDoctorId())
                .patientId(saved.getPatientId())
                .amount(saved.getAmount())
                .build();

        kafkaTemplate.send("AppointmentCreated", event);
        System.out.println("Published AppointmentCreatedEvent: " + event);
        return saved;
    }

    @KafkaListener(topics = "PaymentCompleted", groupId = "AppointmentGroup")
    public void handlePaymentCompleted(PaymentCompletedEvent event)
    {
        Appointment appointment = appointmentRepository.findById(event.getAppointmentId())
                .orElseThrow();
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
    }

    @KafkaListener(topics = "PaymentFailed", groupId = "AppointmentGroup")
    public void handlePaymentFailed(PaymentFailedEvent event)
    {
        Appointment appointment = appointmentRepository.findById(event.getAppointmentId())
                .orElseThrow();
        appointment.setStatus(AppointmentStatus.FAILED);
        appointmentRepository.save(appointment);
    }

    //Get all appointments for a patient
    public List<Appointment> getAppointmentsByPatient(Long patientId)
    {
        return appointmentRepository.findByPatientId(patientId);
    }

    public boolean cancelAppointment(Long appointmentId)
    {
        Optional<Appointment> apt = appointmentRepository.findById(appointmentId);
        if(apt.isPresent()){
            Appointment appointment = apt.get();
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
            return true;
        }
        return false;
    }

    //Get all appointments for a doctor
    public List<Appointment> getAppointmentsByDoctor(Long doctorId)
    {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    //Update appointment status (Doctor)
    public Appointment updateStatus(Long appointmentId, AppointmentStatus status)
    {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
}
