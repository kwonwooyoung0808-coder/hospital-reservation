package com.example.hospital.controller;

import com.example.hospital.exception.DoctorNotFoundException;
import com.example.hospital.exception.DuplicateReservationException;
import com.example.hospital.exception.PatientNotFoundException;
import com.example.hospital.exception.ReservationNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            PatientNotFoundException.class,
            DoctorNotFoundException.class,
            ReservationNotFoundException.class,
            DuplicateReservationException.class
    })
    public String handleBusinessException(RuntimeException exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolation(Model model) {
        model.addAttribute(
                "errorMessage",
                "연결된 예약이 있는 환자 또는 의사는 삭제할 수 없습니다. 예약을 먼저 확인해 주세요."
        );
        return "error";
    }
}
