package com.iss.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserProfileDto
{
    private Long patientId;
    private String username;
    private String email;
    private Set<String> roles;

    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;

    private Long doctorId;
    private String specialization;
    private String licenseNumber;
    private Integer experienceYears;

    private String bloodGroup;
    private String medicalHistory;
    private String profilePictureUrl;
}
