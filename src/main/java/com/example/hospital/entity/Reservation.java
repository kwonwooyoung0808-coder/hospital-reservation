package com.example.hospital.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reservation",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_reservation_doctor_datetime",
                columnNames = {"doctor_id", "reservation_date_time"}
        )
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "reservation_date_time", nullable = false)
    private LocalDateTime reservationDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default 'RESERVED'")
    private ReservationStatus status = ReservationStatus.RESERVED;

    public Reservation() {
    }

    public Reservation(Patient patient,
                       Doctor doctor,
                       LocalDateTime reservationDateTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.reservationDateTime = reservationDateTime;
        this.status = ReservationStatus.RESERVED;
    }

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @PrePersist
    void applyDefaultStatus() {
        if (status == null) {
            status = ReservationStatus.RESERVED;
        }
    }
}
