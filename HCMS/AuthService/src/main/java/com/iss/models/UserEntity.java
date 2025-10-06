package com.iss.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    // Basic account info
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    // User roles (stored in separate table "user_roles")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles; // e.g., ["ROLE_PATIENT", "ROLE_DOCTOR"]

    // Personal info
    private String firstName;
    private String lastName;
    private String gender; // "MALE", "FEMALE", "OTHER"
    private LocalDate dateOfBirth;
    private String phone;
    private String address;

    // Doctor-specific info
    private String specialization;    // Only for doctors
    private String licenseNumber;     // Only for doctors
    private Integer experienceYears;  // Only for doctors

    // Patient-specific info
    private String bloodGroup;        // Only for patients
    @Column(length = 2000)
    private String medicalHistory;    // JSON string or free text

    // Status
    private String status;            // "Active" | "Inactive"

    // Profile picture
    @Column(length = 500)
    private String profilePictureUrl; // Relative path to uploaded image, e.g., "/profile-pictures/drsmith.png"
}
