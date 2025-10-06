package com.iss.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.iss.dtos.UserDto;
import com.iss.dtos.UserProfileDto;
import com.iss.models.RefreshTokenEntity;
import com.iss.models.UserEntity;
import com.iss.repositories.UserRepository;
import com.iss.securities.JwtUtil;
import com.iss.securities.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    /*POST http://localhost:9087/api/auth/register
    Body
    {
      "username": "drsmith",
      "password": "doc12345",
      "email": "drsmith@example.com",
      "roles": ["DOCTOR"],
      "firstName": "Alice",
      "lastName": "Smith",
      "gender": "FEMALE",
      "dateOfBirth": "1980-05-20",
      "phone": "9876543210",
      "address": "456 Hospital Ave",
      "specialization": "Cardiology",
      "licenseNumber": "DOC123456",
      "experienceYears": 10,
      //if patient
      "bloodGroup": "O+",
      "medicalHistory": "None",
      "profilePictureUrl": ""
    }*/
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestPart("user") String userJson,
            @RequestPart(value = "file", required = false) MultipartFile file)
    {
        try{
            //deserialize JSON -> UserEntity
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //optional, makes dates ISO-8601
            UserEntity user = mapper.readValue(userJson, UserEntity.class);

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Set<String> allowedRoles = Set.of("PATIENT", "DOCTOR", "ADMIN");
            if(user.getRoles() == null || user.getRoles().isEmpty()){
                user.setRoles(Set.of("ROLE_PATIENT")); // default
            }else{
                user.setRoles(user.getRoles().stream()
                        .filter(r -> allowedRoles.contains(r.replace("ROLE_", "")))
                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                        .collect(Collectors.toSet()));
            }

            if(file != null && !file.isEmpty()){
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                String uploadDir = "D:/your-project/profile-pictures/";
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) uploadPath.mkdirs();
                file.transferTo(new File(uploadDir, filename));
                user.setProfilePictureUrl("/profile-pictures/" + filename); // relative path for frontend
            }

            userRepository.save(user);

            String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRoles());
            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
            if(refreshToken == null || refreshToken.getToken() == null){
                throw new IllegalStateException("Failed to create refresh token");
            }

            return ResponseEntity.ok(Map.of(
                    "access-token", accessToken,
                    "refresh-token", refreshToken.getToken()
            ));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    /*POST http://localhost:9087/api/auth/login
    Body
    {
      "username": "tarak",
      "password": "raj@1234"
    }*/
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto dto)
    {
        try{
            //System.out.println(dto.getUsername()+","+dto.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );

            UserEntity user = userRepository.findByUsername(dto.getUsername()).orElseThrow();

            String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRoles());
            String refreshToken = refreshTokenService.createRefreshToken(user.getUsername()).getToken();

            //include roles in response
            Set<String> roles = user.getRoles().stream()
                    .map(r -> r.replace("ROLE_", "")) //remove "ROLE_" prefix for frontend
                    .collect(Collectors.toSet());
            //System.out.println(roles);
            return ResponseEntity.ok(Map.of(
                    "access-token", accessToken,
                    "refresh-token", refreshToken,
                    "roles", roles
            ));
        }catch(Exception e){
            return ResponseEntity.status(401).body(Map.of("error", "Alas! Invalid username or password"));
        }
    }

    /*POST http://localhost:9087/api/auth/refresh
    Body
    {
      "refresh-token": "refresh.token"
    }*/
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request)
    {
        String refreshToken = request.get("refresh-token");

        return refreshTokenService.rotateRefreshToken(refreshToken)
                .map(rt -> {
                    UserEntity user = userRepository.findByUsername(rt.getUsername()).orElseThrow();
                    String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRoles());
                    return ResponseEntity.ok(Map.of(
                            "access-token", newAccessToken,
                            "refresh-token", rt.getToken()
                    ));
                })
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid or expired refresh token")));
    }

    /*POST http://localhost:9087/api/auth/logout
    Body
    {
        "username": "tarak"
    }*/
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request)
    {
        String username = request.get("username");
        refreshTokenService.deleteByUsername(username);
        return ResponseEntity.ok(Map.of("message", "User logged out successfully"));
    }

    //GET http://localhost:9087/api/auth/profile?username=drsmith
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(@RequestParam String username)
    {
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        UserProfileDto dto = mapToDto(user);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(@RequestBody UserProfileDto dto)
    {
        UserEntity user = userRepository.findByUsername(dto.getUsername()).orElseThrow();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());

        if(user.getRoles().contains("DOCTOR")) {
            user.setSpecialization(dto.getSpecialization());
            user.setLicenseNumber(dto.getLicenseNumber());
            user.setExperienceYears(dto.getExperienceYears());
        }

        if(user.getRoles().contains("PATIENT")) {
            user.setBloodGroup(dto.getBloodGroup());
            user.setMedicalHistory(dto.getMedicalHistory());
        }
        userRepository.save(user);
        return ResponseEntity.ok(mapToDto(user));
    }

    private UserProfileDto mapToDto(UserEntity user)
    {
        UserProfileDto dto = new UserProfileDto();

        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());

        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());

        if(user.getRoles().contains("ROLE_DOCTOR")){
            dto.setDoctorId(user.getId());
            dto.setSpecialization(user.getSpecialization());
            dto.setLicenseNumber(user.getLicenseNumber());
            dto.setExperienceYears(user.getExperienceYears());
        }

        if(user.getRoles().contains("ROLE_PATIENT")){
            dto.setPatientId(user.getId());
            dto.setBloodGroup(user.getBloodGroup());
            dto.setMedicalHistory(user.getMedicalHistory());
        }
        return dto;
    }
}
