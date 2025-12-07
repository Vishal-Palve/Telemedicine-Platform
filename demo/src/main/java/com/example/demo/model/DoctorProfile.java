package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doctor_profiles")
@Data
@NoArgsConstructor
public class DoctorProfile {

    @Id
    private Long id; // This will be the same as the User ID

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // This makes the 'id' field both the primary key and a foreign key
    @JoinColumn(name = "id")
    private User user;

    @NotBlank
    private String specialty;

    private String qualifications;
    private int experienceInYears;
    private double consultationFee;

    public DoctorProfile(User user, String specialty, String qualifications, int experienceInYears, double consultationFee) {
        this.user = user;
        this.specialty = specialty;
        this.qualifications = qualifications;
        this.experienceInYears = experienceInYears;
        this.consultationFee = consultationFee;
    }
}

