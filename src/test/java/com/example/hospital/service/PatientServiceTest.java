package com.example.hospital.service;

import com.example.hospital.entity.Patient;
import com.example.hospital.exception.PatientNotFoundException;
import com.example.hospital.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = new PatientService(patientRepository);
    }

    @Test
    void savesPatient() {
        Patient patient = new Patient("김환자", LocalDate.of(1990, 1, 1), "010-1111-2222");
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient saved = patientService.save(patient);

        assertThat(saved).isSameAs(patient);
        verify(patientRepository).save(patient);
    }

    @Test
    void throwsWhenPatientDoesNotExist() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findById(99L))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessageContaining("99");
    }
}
