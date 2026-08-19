package com.example.hospital.service;

import com.example.hospital.entity.Patient;
import com.example.hospital.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // 환자 전체 조회
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    // 환자 한 명 조회
    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다."));
    }

    // 환자 등록 및 수정
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    // 환자 삭제
    public void delete(Long id) {
        patientRepository.deleteById(id);
    }
}