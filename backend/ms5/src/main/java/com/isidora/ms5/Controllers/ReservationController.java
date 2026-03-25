package com.isidora.ms5.Controllers;

import com.isidora.ms5.Models.TariffEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.isidora.ms5.Entities.ReservationEntity;
import com.isidora.ms5.Repositories.ReservationRepository;
import com.isidora.ms5.Requests.ReservationRequestDTO;
import com.isidora.ms5.Services.ReservationService;

import java.util.List;

@RestController
@RequestMapping("api/reservations")

public class ReservationController {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/all")
    public List<ReservationEntity> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/rt")
    public List<TariffEntity> getAllTariffs() {
        return reservationService.getAllTariffs();
    }

    @PostMapping("/create")
    public ReservationEntity createReservationWithPricing(@RequestBody ReservationRequestDTO request) {
        return reservationService.createReservationWithPricing(
                request.getDate(),
                request.getStartTime(),
                request.getLaps(),
                request.getGroupSize(),
                request.getCustomers()
        );
    }
    @GetMapping("/{id}")
    public ReservationEntity getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }
    @PostMapping("/sendMail/{id}")
    public void sendMailConfirmation(@PathVariable Long id) {
        reservationService.sendMailConfirmation(id);
    }

}
