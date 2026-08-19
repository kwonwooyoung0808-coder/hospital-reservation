package com.example.hospital.service;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Patient;
import com.example.hospital.entity.Reservation;
import com.example.hospital.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

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
            throw new IllegalArgumentException(
                    "이미 해당 시간에 예약이 존재합니다."
            );
        }

        Reservation reservation =
                new Reservation(
                        patient,
                        doctor,
                        reservationDateTime
                );

        return reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}