package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientProfileDto {
    private Long userId;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String insuranceDetails;
}
