package com.example.hospital.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Long id) {
        super("환자를 찾을 수 없습니다. ID: " + id);
    }
}
