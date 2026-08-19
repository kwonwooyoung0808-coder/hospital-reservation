package com.example.hospital.controller;

import com.example.hospital.dto.PatientForm;
import com.example.hospital.entity.Patient;
import com.example.hospital.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String list(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String phone,
            Model model
    ) {
        model.addAttribute("patients", patientService.search(name, phone));
        model.addAttribute("name", name);
        model.addAttribute("phone", phone);
        return "patients/list";
    }

    // 환자 등록 화면
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("patient", new PatientForm());
        return "patients/form";
    }

    // 환자 등록
    @PostMapping
    public String create(
            @Valid @ModelAttribute("patient") PatientForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "patients/form";
        }
        patientService.save(new Patient(form.getName(), form.getBirthDate(), form.getPhone()));
        return "redirect:/patients";
    }

    // 환자 수정 화면
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", PatientForm.from(patientService.findById(id)));
        return "patients/form";
    }

    // 환자 수정
    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("patient") PatientForm form,
            BindingResult bindingResult
    ) {
        form.setId(id);
        if (bindingResult.hasErrors()) {
            return "patients/form";
        }
        Patient existing = patientService.findById(id);

        existing.setName(form.getName());
        existing.setBirthDate(form.getBirthDate());
        existing.setPhone(form.getPhone());

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
