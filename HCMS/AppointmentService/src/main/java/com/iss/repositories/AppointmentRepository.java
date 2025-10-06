package com.iss.repositories;

import com.hcms.common.enums.AppointmentStatus;
import com.iss.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>
{
    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

}
