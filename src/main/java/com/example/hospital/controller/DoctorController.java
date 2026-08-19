package com.example.hospital.controller;

import com.example.hospital.dto.DoctorForm;
import com.example.hospital.entity.Doctor;
import com.example.hospital.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String department,
            Model model
    ) {
        model.addAttribute("doctors", doctorService.search(name, department));
        model.addAttribute("departments", doctorService.getDepartments());
        model.addAttribute("name", name);
        model.addAttribute("department", department);
        return "doctors/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("doctor", new DoctorForm());
        model.addAttribute("departments", doctorService.getDepartments());
        return "doctors/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("doctor") DoctorForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", doctorService.getDepartments());
            return "doctors/form";
        }
        doctorService.save(new Doctor(form.getName(), form.getDepartment()));
        return "redirect:/doctors";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("doctor", DoctorForm.from(doctorService.findById(id)));
        model.addAttribute("departments", doctorService.getDepartments());
        return "doctors/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("doctor") DoctorForm form,
                         BindingResult bindingResult,
                         Model model) {
        form.setId(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", doctorService.getDepartments());
            return "doctors/form";
        }
        Doctor existing = doctorService.findById(id);

        existing.setName(form.getName());
        existing.setDepartment(form.getDepartment());

        doctorService.save(existing);

        return "redirect:/doctors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        doctorService.delete(id);
        return "redirect:/doctors";
    }
}
