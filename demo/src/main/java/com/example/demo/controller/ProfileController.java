package com.example.demo.controller;

import com.example.demo.dto.DoctorProfileDto;
import com.example.demo.dto.PatientProfileDto;
import com.example.demo.security.services.UserDetailsImpl;
import com.example.demo.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profiles", description = "Endpoints for managing user profiles (Doctor and Patient)")
@RestController
@RequestMapping("/api/profiles")
@SecurityRequirement(name = "bearerAuth") // This applies JWT security to all endpoints in this controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // --- Doctor Profile Endpoints ---

    @Operation(
            summary = "Create or Update Doctor Profile",
            description = "Allows a logged-in doctor to create or update their own professional profile. Requires ROLE_DOCTOR.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User is not a doctor"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
            }
    )
    @PostMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createOrUpdateDoctorProfile(@Valid @RequestBody DoctorProfileDto profileDto) {
        Long userId = getCurrentUserId();
        profileService.createOrUpdateDoctorProfile(userId, profileDto);
        return ResponseEntity.ok("Doctor profile updated successfully.");
    }

    @Operation(
            summary = "Get Doctor Profile by ID",
            description = "Allows any authenticated user (Patient or Doctor) to view a doctor's public profile.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved profile"),
                    @ApiResponse(responseCode = "404", description = "Profile not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
            }
    )
    @GetMapping("/doctor/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<DoctorProfileDto> getDoctorProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getDoctorProfile(id));
    }

    // --- Patient Profile Endpoints ---

    @Operation(
            summary = "Create or Update Patient Profile",
            description = "Allows a logged-in patient to create or update their own personal profile. Requires ROLE_PATIENT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User is not a patient"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
            }
    )
    @PostMapping("/patient")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> createOrUpdatePatientProfile(@Valid @RequestBody PatientProfileDto profileDto) {
        Long userId = getCurrentUserId();
        profileService.createOrUpdatePatientProfile(userId, profileDto);
        return ResponseEntity.ok("Patient profile updated successfully.");
    }

    @Operation(
            summary = "Get Patient Profile by ID",
            description = "Allows a doctor to view any patient's profile, or a patient to view their own. Access is restricted for other roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved profile"),
                    @ApiResponse(responseCode = "404", description = "Profile not found"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User is not authorized to view this profile"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
            }
    )
    @GetMapping("/patient/{id}")
    @PreAuthorize("hasRole('DOCTOR') or #id == authentication.principal.id") // Security rule: Doctor can see any patient, patient can only see their own
    public ResponseEntity<PatientProfileDto> getPatientProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getPatientProfile(id));
    }

    /**
     * Helper method to get the user ID from the current security context.
     * @return The ID of the currently authenticated user.
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}
