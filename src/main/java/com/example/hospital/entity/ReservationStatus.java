package com.example.hospital.entity;

public enum ReservationStatus {
    RESERVED("예약"),
    COMPLETED("진료 완료"),
    CANCELLED("취소");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
