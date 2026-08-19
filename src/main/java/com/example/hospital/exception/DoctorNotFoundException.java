package com.example.hospital.exception;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(Long id) {
        super("의사를 찾을 수 없습니다. ID: " + id);
    }
}
