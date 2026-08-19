package com.example.hospital.dto;

import com.example.hospital.entity.Doctor;
import jakarta.validation.constraints.NotBlank;

public class DoctorForm {

    private Long id;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "진료과를 선택해 주세요.")
    private String department;

    public DoctorForm() {
    }

    public static DoctorForm from(Doctor doctor) {
        DoctorForm form = new DoctorForm();
        form.id = doctor.getId();
        form.name = doctor.getName();
        form.department = doctor.getDepartment();
        return form;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
}
