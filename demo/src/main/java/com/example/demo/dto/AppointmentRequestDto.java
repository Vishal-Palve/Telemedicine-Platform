package com.example.demo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequestDto {
    @NotNull
    private Long doctorId;

    @NotNull
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDateTime;
}
