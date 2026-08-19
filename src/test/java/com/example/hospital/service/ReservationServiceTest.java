package com.example.hospital.service;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Patient;
import com.example.hospital.entity.Reservation;
import com.example.hospital.entity.ReservationStatus;
import com.example.hospital.exception.DuplicateReservationException;
import com.example.hospital.exception.ReservationNotFoundException;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock private ReservationRepository reservationRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;

    private ReservationService reservationService;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime reservationTime;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                reservationRepository, patientRepository, doctorRepository
        );
        patient = new Patient("김환자", LocalDate.of(1990, 1, 1), "010-1111-2222");
        doctor = new Doctor("이의사", "내과");
        reservationTime = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0);
    }

    @Test
    void createsReservationNormally() {
        when(reservationRepository.existsByDoctorAndReservationDateTime(doctor, reservationTime))
                .thenReturn(false);
        when(reservationRepository.saveAndFlush(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation saved = reservationService.createReservation(patient, doctor, reservationTime);

        assertThat(saved.getPatient()).isSameAs(patient);
        assertThat(saved.getDoctor()).isSameAs(doctor);
        assertThat(saved.getReservationDateTime()).isEqualTo(reservationTime);
        assertThat(saved.getStatus()).isEqualTo(ReservationStatus.RESERVED);
    }

    @Test
    void rejectsSameDoctorAtSameTime() {
        when(reservationRepository.existsByDoctorAndReservationDateTime(doctor, reservationTime))
                .thenReturn(true);

        assertThatThrownBy(() -> reservationService.createReservation(patient, doctor, reservationTime))
                .isInstanceOf(DuplicateReservationException.class);
        verify(reservationRepository, never()).saveAndFlush(any(Reservation.class));
    }

    @Test
    void throwsWhenReservationDoesNotExist() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.findById(99L))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining("99");
    }
}
