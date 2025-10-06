package com.iss.controllers;

import com.iss.models.UserEntity;
import com.iss.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController
{
    private final UserRepository userRepository;

    // GET all patients
    @GetMapping("/getAll")
    public ResponseEntity<List<UserEntity>> getAllPatients()
    {
        List<UserEntity> patients = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains("ROLE_PATIENT"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    // GET patient by username
    @GetMapping("/get/{username}")
    public ResponseEntity<UserEntity> getPatient(@PathVariable String username)
    {
        return userRepository.findByUsername(username)
                .filter(u -> u.getRoles().contains("ROLE_PATIENT"))
                .map(ResponseEntity::ok)                  // 200 OK if found
                .orElse(ResponseEntity.notFound().build()); // 404 if not found
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserEntity> updatePatient(@PathVariable Long id, @RequestBody UserEntity updatedPatient)
    {
        UserEntity patient = userRepository.findById(id)
                .filter(u -> u.getRoles().contains("ROLE_PATIENT"))
                .orElseThrow();

        // update fields
        patient.setUsername(updatedPatient.getUsername());
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setPhone(updatedPatient.getPhone());
        patient.setAddress(updatedPatient.getAddress());
        patient.setBloodGroup(updatedPatient.getBloodGroup());
        patient.setMedicalHistory(updatedPatient.getMedicalHistory());
        patient.setStatus(updatedPatient.getStatus());
        patient.setProfilePictureUrl(updatedPatient.getProfilePictureUrl());

        userRepository.save(patient);
        return ResponseEntity.ok(patient);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id)
    {
        UserEntity patient = userRepository.findById(id)
                .filter(u -> u.getRoles().contains("ROLE_PATIENT"))
                .orElseThrow();

        userRepository.delete(patient);
        return ResponseEntity.noContent().build();
    }

}
