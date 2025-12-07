package com.example.demo.controller;

import com.example.demo.dto.DoctorProfileDto;
import com.example.demo.dto.PatientProfileDto;
import com.example.demo.security.services.UserDetailsImpl;
import com.example.demo.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize; // Temporarily removed
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/doctor")
    // @PreAuthorize("hasRole('DOCTOR')") // Temporarily removed
    public ResponseEntity<?> createOrUpdateDoctorProfile(@Valid @RequestBody DoctorProfileDto profileDto) {
        Long userId = getCurrentUserId();
        profileService.createOrUpdateDoctorProfile(userId, profileDto);
        return ResponseEntity.ok("Doctor profile updated successfully.");
    }

    @GetMapping("/doctor/{id}")
    // @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR')") // Temporarily removed
    public ResponseEntity<DoctorProfileDto> getDoctorProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getDoctorProfile(id));
    }

    @PostMapping("/patient")
    // @PreAuthorize("hasRole('PATIENT')") // Temporarily removed
    public ResponseEntity<?> createOrUpdatePatientProfile(@Valid @RequestBody PatientProfileDto profileDto) {
        Long userId = getCurrentUserId();
        profileService.createOrUpdatePatientProfile(userId, profileDto);
        return ResponseEntity.ok("Patient profile updated successfully.");
    }

    @GetMapping("/patient/{id}")
    // @PreAuthorize("hasRole('DOCTOR') or #id == authentication.principal.id") // Temporarily removed
    public ResponseEntity<PatientProfileDto> getPatientProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getPatientProfile(id));
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}