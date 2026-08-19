package com.example.hospital.controller;

import com.example.hospital.entity.Patient;
import com.example.hospital.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // 환자 목록 화면
    @GetMapping
    public String list(Model model) {
        model.addAttribute("patients", patientService.findAll());
        return "patients/list";
    }

    // 환자 등록 화면
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patients/form";
    }

    // 환자 등록
    @PostMapping
    public String create(@ModelAttribute Patient patient) {
        patientService.save(patient);
        return "redirect:/patients";
    }

    // 환자 수정 화면
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.findById(id));
        return "patients/form";
    }

    // 환자 수정
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Patient patient) {
        Patient existing = patientService.findById(id);

        existing.setName(patient.getName());
        existing.setBirthDate(patient.getBirthDate());
        existing.setPhone(patient.getPhone());

        patientService.save(existing);

        return "redirect:/patients";
    }

    // 환자 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        patientService.delete(id);
        return "redirect:/patients";
    }
}