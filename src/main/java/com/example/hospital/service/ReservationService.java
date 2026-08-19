package com.example.hospital.service;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Patient;
import com.example.hospital.entity.Reservation;
import com.example.hospital.entity.ReservationStatus;
import com.example.hospital.exception.DoctorNotFoundException;
import com.example.hospital.exception.DuplicateReservationException;
import com.example.hospital.exception.PatientNotFoundException;
import com.example.hospital.exception.ReservationNotFoundException;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.repository.ReservationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<Reservation> findAll() {
        return search(null, null, null, null);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public List<Reservation> search(
            String patientName,
            String doctorName,
            ReservationStatus status,
            LocalDate reservationDate
    ) {
        LocalDateTime start = reservationDate == null ? null : reservationDate.atStartOfDay();
        LocalDateTime end = reservationDate == null ? null : reservationDate.plusDays(1).atStartOfDay();
        return reservationRepository.search(
                blankToNull(patientName), blankToNull(doctorName), status, start, end
        );
    }

    @Transactional
    public Reservation createReservation(
            Long patientId,
            Long doctorId,
            LocalDateTime reservationDateTime
    ) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
        return createReservation(patient, doctor, reservationDateTime);
    }

    @Transactional
    public Reservation createReservation(
            Patient patient,
            Doctor doctor,
            LocalDateTime reservationDateTime
    ) {

        boolean duplicated =
                reservationRepository
                        .existsByDoctorAndReservationDateTime(
                                doctor,
                                reservationDateTime
                        );

        if (duplicated) {
            throw new DuplicateReservationException();
        }

        Reservation reservation =
                new Reservation(
                        patient,
                        doctor,
                        reservationDateTime
                );

        try {
            return reservationRepository.saveAndFlush(reservation);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateReservationException();
        }
    }

    @Transactional
    public Reservation changeStatus(Long id, ReservationStatus status) {
        Reservation reservation = findById(id);
        reservation.setStatus(status);
        return reservation;
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.delete(findById(id));
    }

    public long count() {
        return reservationRepository.count();
    }

    public long countToday() {
        LocalDate today = LocalDate.now();
        return reservationRepository
                .countByReservationDateTimeGreaterThanEqualAndReservationDateTimeLessThan(
                        today.atStartOfDay(), today.plusDays(1).atStartOfDay()
                );
    }

    public List<Reservation> findRecentFive() {
        return reservationRepository.findTop5ByOrderByReservationDateTimeDesc();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
