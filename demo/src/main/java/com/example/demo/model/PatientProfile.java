package com.example.demo.model;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "patient_profiles")
@Data
@NoArgsConstructor
public class PatientProfile {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private LocalDate dateOfBirth;
    private String address;
    private String insuranceDetails;

    public PatientProfile(User user, LocalDate dateOfBirth, String address, String insuranceDetails) {
        this.user = user;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.insuranceDetails = insuranceDetails;
    }
}

