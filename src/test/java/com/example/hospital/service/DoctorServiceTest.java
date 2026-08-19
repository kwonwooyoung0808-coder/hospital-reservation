package com.example.hospital.service;

import com.example.hospital.entity.Doctor;
import com.example.hospital.exception.DoctorNotFoundException;
import com.example.hospital.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        doctorService = new DoctorService(doctorRepository);
    }

    @Test
    void savesDoctor() {
        Doctor doctor = new Doctor("이의사", "내과");
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor saved = doctorService.save(doctor);

        assertThat(saved).isSameAs(doctor);
        verify(doctorRepository).save(doctor);
    }

    @Test
    void throwsWhenDoctorDoesNotExist() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.findById(99L))
                .isInstanceOf(DoctorNotFoundException.class)
                .hasMessageContaining("99");
    }
}
