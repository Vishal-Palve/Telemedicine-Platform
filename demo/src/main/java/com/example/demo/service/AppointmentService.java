package com.example.demo.service;

import com.example.demo.dto.AppointmentRequestDto;
import com.example.demo.dto.AppointmentResponseDto;
import com.example.demo.model.Appointment;
import com.example.demo.model.AppointmentStatus;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AppointmentResponseDto bookAppointment(Long patientId, AppointmentRequestDto requestDto) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new UsernameNotFoundException("Patient not found with id: " + patientId));

        User doctor = userRepository.findById(requestDto.getDoctorId())
                .orElseThrow(() -> new UsernameNotFoundException("Doctor not found with id: " + requestDto.getDoctorId()));

        if (doctor.getRole() != Role.ROLE_DOCTOR) {
            throw new IllegalArgumentException("Invalid doctor ID provided.");
        }

        Appointment appointment = new Appointment(doctor, patient, requestDto.getAppointmentDateTime());
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return convertToDto(savedAppointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getAppointmentsForDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponseDto cancelAppointment(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the user canceling is either the patient or the doctor of the appointment
        if (!appointment.getPatient().getId().equals(userId) && !appointment.getDoctor().getId().equals(userId)) {
            throw new SecurityException("User is not authorized to cancel this appointment");
        }

        if(user.getRole() == Role.ROLE_PATIENT) {
            appointment.setStatus(AppointmentStatus.CANCELED_BY_PATIENT);
        } else {
            appointment.setStatus(AppointmentStatus.CANCELED_BY_DOCTOR);
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return convertToDto(updatedAppointment);
    }


    private AppointmentResponseDto convertToDto(Appointment appointment) {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setId(appointment.getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setDoctorName(appointment.getDoctor().getUsername());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setPatientName(appointment.getPatient().getUsername());
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        dto.setStatus(appointment.getStatus());
        dto.setConsultationNotes(appointment.getConsultationNotes());
        return dto;
    }
}
