package com.example.demo.service;

import com.example.demo.dto.DoctorProfileDto;
import com.example.demo.dto.PatientProfileDto;
import com.example.demo.model.DoctorProfile;
import com.example.demo.model.PatientProfile;
import com.example.demo.model.User;
import com.example.demo.repository.DoctorProfileRepository;
import com.example.demo.repository.PatientProfileRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private PatientProfileRepository patientProfileRepository;

    @Transactional
    public DoctorProfile createOrUpdateDoctorProfile(Long userId, DoctorProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        DoctorProfile profile = doctorProfileRepository.findById(userId)
                .orElse(new DoctorProfile());

        profile.setUser(user);
        profile.setSpecialty(profileDto.getSpecialty());
        profile.setQualifications(profileDto.getQualifications());
        profile.setExperienceInYears(profileDto.getExperienceInYears());
        profile.setConsultationFee(profileDto.getConsultationFee());

        return doctorProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public DoctorProfileDto getDoctorProfile(Long userId) {
        DoctorProfile profile = doctorProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        return convertToDto(profile);
    }

    // Similar methods for PatientProfile
    @Transactional
    public PatientProfile createOrUpdatePatientProfile(Long userId, PatientProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        PatientProfile profile = patientProfileRepository.findById(userId)
                .orElse(new PatientProfile());

        profile.setUser(user);
        profile.setDateOfBirth(profileDto.getDateOfBirth());
        profile.setAddress(profileDto.getAddress());
        profile.setInsuranceDetails(profileDto.getInsuranceDetails());

        return patientProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public PatientProfileDto getPatientProfile(Long userId) {
        PatientProfile profile = patientProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        return convertToDto(profile);
    }

    // Helper methods to convert Entity to DTO
    private DoctorProfileDto convertToDto(DoctorProfile profile) {
        DoctorProfileDto dto = new DoctorProfileDto();
        dto.setUserId(profile.getUser().getId());
        dto.setUsername(profile.getUser().getUsername());
        dto.setEmail(profile.getUser().getEmail());
        dto.setSpecialty(profile.getSpecialty());
        dto.setQualifications(profile.getQualifications());
        dto.setExperienceInYears(profile.getExperienceInYears());
        dto.setConsultationFee(profile.getConsultationFee());
        return dto;
    }

    private PatientProfileDto convertToDto(PatientProfile profile) {
        PatientProfileDto dto = new PatientProfileDto();
        dto.setUserId(profile.getUser().getId());
        dto.setUsername(profile.getUser().getUsername());
        dto.setEmail(profile.getUser().getEmail());
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setAddress(profile.getAddress());
        dto.setInsuranceDetails(profile.getInsuranceDetails());
        return dto;
    }
}


