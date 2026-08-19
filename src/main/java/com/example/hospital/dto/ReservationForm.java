package com.example.hospital.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ReservationForm {

    @NotNull(message = "환자를 선택해 주세요.")
    private Long patientId;

    @NotNull(message = "의사를 선택해 주세요.")
    private Long doctorId;

    @NotNull(message = "예약 시간을 입력해 주세요.")
    @Future(message = "예약 시간은 현재 이후여야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime reservationDateTime;

    public Long getPatientId() { return patientId; }
    public Long getDoctorId() { return doctorId; }
    public LocalDateTime getReservationDateTime() { return reservationDateTime; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public void setReservationDateTime(LocalDateTime reservationDateTime) { this.reservationDateTime = reservationDateTime; }
}
