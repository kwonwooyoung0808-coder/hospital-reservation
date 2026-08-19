package com.example.hospital.exception;

public class DuplicateReservationException extends RuntimeException {
    public DuplicateReservationException() {
        super("이미 해당 시간에 이 의사의 예약이 존재합니다.");
    }
}
