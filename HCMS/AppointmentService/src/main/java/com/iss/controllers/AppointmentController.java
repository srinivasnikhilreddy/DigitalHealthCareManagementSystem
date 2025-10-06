package com.iss.controllers;

import com.hcms.common.enums.AppointmentStatus;
import com.iss.models.Appointment;
import com.iss.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController
{
    @Autowired
    private AppointmentService appointmentService;

    /*POST http://localhost:9081/appointments/schedule
    Body
    {
      "patientId": 10004,
      "patientName": "John Doe",
      "patientDob": "1990-05-20",
      "doctorId": 10005,
      "requestDate": "2025-10-01T14:00:00",
      "appointmentDate": "2025-10-10T10:30:00",
      "type": "Consultation",
      "slot": "SLOT_10_11",
      "status": "SCHEDULED"
    }*/
    @PostMapping("/schedule")
    public ResponseEntity<Appointment> schedule(@RequestBody @Valid Appointment appointment)
    {
        Appointment booked = appointmentService.bookAppointment(appointment);
        return ResponseEntity.ok(booked);
    }

    //GET http://localhost:9081/appointments/patient/1
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        if(appointments.isEmpty()){
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(appointments); // 200 OK with body
    }

    //DELETE http://localhost:9081/appointments/1/cancel -> 204 No Content (means appointment CANCELLED)
    @DeleteMapping("/{appointmentId}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        boolean cancelled = appointmentService.cancelAppointment(appointmentId);
        if(cancelled){
            return ResponseEntity.noContent().build(); // 204 No Content
        }else{
            return ResponseEntity.notFound().build(); // 404 if not found
        }
    }

    //GET http://localhost:9081/appointments/doctor/1
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable Long doctorId)
    {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        if(appointments.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(appointments);
    }

    //PUT http://localhost:9081/appointments/4/status?status=COMPLETED
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Appointment> updateStatus(@PathVariable Long appointmentId,
                                                    @RequestParam AppointmentStatus status)
    {
        Appointment updated = appointmentService.updateStatus(appointmentId, status);
        return ResponseEntity.ok(updated);
    }
    
}
