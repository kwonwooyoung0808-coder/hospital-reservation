package com.example.hospital.dto;

import com.example.hospital.entity.Patient;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class PatientForm {

    private Long id;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    private LocalDate birthDate;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phone;

    public PatientForm() {
    }

    public static PatientForm from(Patient patient) {
        PatientForm form = new PatientForm();
        form.id = patient.getId();
        form.name = patient.getName();
        form.birthDate = patient.getBirthDate();
        form.phone = patient.getPhone();
        return form;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getPhone() { return phone; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setPhone(String phone) { this.phone = phone; }
}
