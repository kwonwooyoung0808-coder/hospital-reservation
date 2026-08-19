package com.example.hospital.controller;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Patient;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.PatientService;
import com.example.hospital.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    public String list(Model model) {

        model.addAttribute(
                "reservations",
                reservationService.findAll()
        );

        return "reservations/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {

        model.addAttribute(
                "patients",
                patientService.findAll()
        );

        model.addAttribute(
                "doctors",
                doctorService.findAll()
        );

        return "reservations/form";
    }

    @PostMapping
    public String create(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime reservationDateTime,
            Model model
    ) {

        Patient patient =
                patientService.findById(patientId);

        Doctor doctor =
                doctorService.findById(doctorId);

        try {

            reservationService.createReservation(
                    patient,
                    doctor,
                    reservationDateTime
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("doctors", doctorService.findAll());

            return "reservations/form";
        }

        return "redirect:/reservations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {

        reservationService.delete(id);

        return "redirect:/reservations";
    }
}