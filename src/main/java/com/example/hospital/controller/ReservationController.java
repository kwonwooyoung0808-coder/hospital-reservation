package com.example.hospital.controller;

import com.example.hospital.dto.ReservationForm;
import com.example.hospital.entity.ReservationStatus;
import com.example.hospital.exception.DuplicateReservationException;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.PatientService;
import com.example.hospital.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public ReservationController(
            ReservationService reservationService,
            PatientService patientService,
            DoctorService doctorService
    ) {
        this.reservationService = reservationService;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String patientName,
            @RequestParam(defaultValue = "") String doctorName,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
            Model model
    ) {
        model.addAttribute("reservations", reservationService.search(
                patientName, doctorName, status, reservationDate
        ));
        model.addAttribute("statuses", ReservationStatus.values());
        model.addAttribute("patientName", patientName);
        model.addAttribute("doctorName", doctorName);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("reservationDate", reservationDate);
        return "reservations/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("reservation", new ReservationForm());
        addFormOptions(model);
        return "reservations/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("reservation") ReservationForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            addFormOptions(model);
            return "reservations/form";
        }
        try {
            reservationService.createReservation(
                    form.getPatientId(),
                    form.getDoctorId(),
                    form.getReservationDateTime()
            );
        } catch (DuplicateReservationException e) {
            bindingResult.reject("duplicate", e.getMessage());
            addFormOptions(model);
            return "reservations/form";
        }
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        reservationService.delete(id);
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status
    ) {
        reservationService.changeStatus(id, status);
        return "redirect:/reservations";
    }

    private void addFormOptions(Model model) {
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("doctors", doctorService.findAll());
    }
}
