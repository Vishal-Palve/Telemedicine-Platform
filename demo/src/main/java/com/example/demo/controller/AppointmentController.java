package com.example.demo.controller;

import com.example.demo.dto.AppointmentRequestDto;
import com.example.demo.dto.AppointmentResponseDto;
import com.example.demo.security.services.UserDetailsImpl;
import com.example.demo.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(@Valid @RequestBody AppointmentRequestDto requestDto) {
        Long patientId = getCurrentUserId();
        AppointmentResponseDto newAppointment = appointmentService.bookAppointment(patientId, requestDto);
        return ResponseEntity.ok(newAppointment);
    }

    @GetMapping("/my-appointments")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments() {
        UserDetailsImpl userDetails = getCurrentUserDetails();
        List<AppointmentResponseDto> appointments;

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            appointments = appointmentService.getAppointmentsForPatient(userDetails.getId());
        } else {
            appointments = appointmentService.getAppointmentsForDoctor(userDetails.getId());
        }
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<AppointmentResponseDto> cancelAppointment(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        AppointmentResponseDto canceledAppointment = appointmentService.cancelAppointment(id, userId);
        return ResponseEntity.ok(canceledAppointment);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    private UserDetailsImpl getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
