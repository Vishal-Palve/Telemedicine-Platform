package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 25, nullable = false)
    private AppointmentStatus status;

    private String consultationNotes; // Can be filled in by the doctor after completion

    public Appointment(User doctor, User patient, LocalDateTime appointmentDateTime) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.SCHEDULED;
    }
}