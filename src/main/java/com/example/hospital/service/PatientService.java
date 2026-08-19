package com.example.hospital.service;

import com.example.hospital.entity.Patient;
import com.example.hospital.exception.PatientNotFoundException;
import com.example.hospital.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // 환자 전체 조회
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public List<Patient> search(String name, String phone) {
        return patientRepository.findByNameContainingIgnoreCaseAndPhoneContaining(
                normalize(name), normalize(phone)
        );
    }

    // 환자 한 명 조회
    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    // 환자 등록 및 수정
    @Transactional
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    // 환자 삭제
    @Transactional
    public void delete(Long id) {
        patientRepository.delete(findById(id));
    }

    public long count() {
        return patientRepository.count();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
