package com.iss.controllers;

import com.iss.models.UserEntity;
import com.iss.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor //Lombok annotation that automatically generates a constructor for all final fields and fields annotated with @NonNull in the class.
public class UserController
{
    private final UserRepository userRepository;

    /*GET http://localhost:9087/api/info
    Authorization -> Bearer Token -> jwt.token*/
    @GetMapping("/info")
    public ResponseEntity<String> getUserDetails()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if(principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails){
            username = userDetails.getUsername();
        }else{
            username = principal.toString();
        }

        return ResponseEntity.ok(userRepository.findByUsername(username)
                .map(UserEntity::getUsername)
                .orElse("Unknown user"));
    }

    /*GET http://localhost:9087/api/patient
    Authorization -> Bearer Token -> jwt.token*/
    @GetMapping("/patient")
    public ResponseEntity<String> getPatientDashboard()
    {
        return ResponseEntity.ok("<h2>Welcome, Patient Dashboard!</h2>");
    }

    /*GET http://localhost:9087/api/doctor
    Authorization -> Bearer Token -> jwt.token*/
    @GetMapping("/doctor")
    public ResponseEntity<String> getDoctorDashboard()
    {
        return ResponseEntity.ok("<h2>Welcome, Doctor Dashboard!</h2>");
    }

    /*GET http://localhost:9087/api/admin
    Authorization -> Bearer Token -> jwt.token*/
    @GetMapping("/admin")
    public ResponseEntity<String> getAdminDashboard()
    {
        return ResponseEntity.ok("<h2>Welcome, Admin Dashboard!</h2>");
    }
}
