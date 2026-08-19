package com.example.hospital.service;

import com.example.hospital.entity.Doctor;
import com.example.hospital.exception.DoctorNotFoundException;
import com.example.hospital.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DoctorService {

    private static final List<String> DEPARTMENTS = List.of(
            "내과", "외과", "정형외과", "소아청소년과", "피부과"
    );

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public List<Doctor> search(String name, String department) {
        String normalizedName = name == null ? "" : name.trim();
        if (department == null || department.isBlank()) {
            return doctorRepository.findByNameContainingIgnoreCase(normalizedName);
        }
        return doctorRepository.findByNameContainingIgnoreCaseAndDepartment(
                normalizedName, department
        );
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    @Transactional
    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void delete(Long id) {
        doctorRepository.delete(findById(id));
    }

    public long count() {
        return doctorRepository.count();
    }

    public List<String> getDepartments() {
        return DEPARTMENTS;
    }
}
