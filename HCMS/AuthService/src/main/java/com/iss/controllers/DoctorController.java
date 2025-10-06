package com.iss.controllers;

import com.iss.models.UserEntity;
import com.iss.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController
{
    private final UserRepository userRepository;

    // GET all doctors
    @GetMapping("/getAll")
    public ResponseEntity<List<UserEntity>> getAllDoctors()
    {
        List<UserEntity> doctors = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains("ROLE_DOCTOR"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    // GET doctor by username
    @GetMapping("/get/{username}")
    public ResponseEntity<UserEntity> getDoctor(@PathVariable String username)
    {
        UserEntity doctor = userRepository.findByUsername(username)
                .filter(u -> u.getRoles().contains("ROLE_DOCTOR"))
                .orElseThrow();
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserEntity> updateDoctor(@PathVariable Long id, @RequestBody UserEntity updatedDoctor)
    {
        UserEntity doctor = userRepository.findById(id)
                .filter(u -> u.getRoles().contains("ROLE_DOCTOR"))
                .orElseThrow();

        // update fields
        doctor.setFirstName(updatedDoctor.getFirstName());
        doctor.setLastName(updatedDoctor.getLastName());
        doctor.setPhone(updatedDoctor.getPhone());
        doctor.setAddress(updatedDoctor.getAddress());        doctor.setSpecialization(updatedDoctor.getSpecialization());
        doctor.setLicenseNumber(updatedDoctor.getLicenseNumber());
        doctor.setExperienceYears(updatedDoctor.getExperienceYears());
        doctor.setProfilePictureUrl(updatedDoctor.getProfilePictureUrl());

        userRepository.save(doctor);
        return ResponseEntity.ok(doctor);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id)
    {
        UserEntity doctor = userRepository.findById(id)
                .filter(u -> u.getRoles().contains("ROLE_DOCTOR"))
                .orElseThrow();

        userRepository.delete(doctor);
        return ResponseEntity.noContent().build();
    }

}
