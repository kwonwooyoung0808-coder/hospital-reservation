package com.example.hospital.controller;

import com.example.hospital.service.DoctorService;
import com.example.hospital.service.PatientService;
import com.example.hospital.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ReservationService reservationService;

    public DashboardController(
            PatientService patientService,
            DoctorService doctorService,
            ReservationService reservationService
    ) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("patientCount", patientService.count());
        model.addAttribute("doctorCount", doctorService.count());
        model.addAttribute("reservationCount", reservationService.count());
        model.addAttribute("todayReservationCount", reservationService.countToday());
        model.addAttribute("recentReservations", reservationService.findRecentFive());
        model.addAttribute("today", LocalDate.now());
        return "dashboard";
    }
}
