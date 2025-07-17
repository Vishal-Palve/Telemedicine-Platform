package com.example.demo.dto;

import com.example.demo.model.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponseDto {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String consultationNotes;
}
