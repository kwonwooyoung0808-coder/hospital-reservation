package com.example.hospital.repository;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Reservation;
import com.example.hospital.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByDoctorAndReservationDateTime(
            Doctor doctor,
            LocalDateTime reservationDateTime
    );

    long countByReservationDateTimeGreaterThanEqualAndReservationDateTimeLessThan(
            LocalDateTime start, LocalDateTime end
    );

    @EntityGraph(attributePaths = {"patient", "doctor"})
    List<Reservation> findTop5ByOrderByReservationDateTimeDesc();

    @Query("""
            select r from Reservation r
            join fetch r.patient p
            join fetch r.doctor d
            where (:patientName is null or lower(p.name) like lower(concat('%', :patientName, '%')))
              and (:doctorName is null or lower(d.name) like lower(concat('%', :doctorName, '%')))
              and (:status is null or r.status = :status)
              and (:startDateTime is null or r.reservationDateTime >= :startDateTime)
              and (:endDateTime is null or r.reservationDateTime < :endDateTime)
            order by r.reservationDateTime desc
            """)
    List<Reservation> search(
            @Param("patientName") String patientName,
            @Param("doctorName") String doctorName,
            @Param("status") ReservationStatus status,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}
