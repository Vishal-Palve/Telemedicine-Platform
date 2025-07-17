package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DoctorProfileDto {
    private Long userId;
    private String username;
    private String email;

    @NotBlank
    private String specialty;
    private String qualifications;

    @PositiveOrZero
    private int experienceInYears;

    @Positive
    private double consultationFee;
}