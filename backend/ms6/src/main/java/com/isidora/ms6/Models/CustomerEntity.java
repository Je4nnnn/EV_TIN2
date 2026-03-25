package com.isidora.ms6.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

public class CustomerEntity {
    private Long id_customer;

    private String name;
    private String lastname;
    private String rut;
    private String email;
    private String phone;
    private LocalDate birthdate;
    private LocalDate visitDate;
    private ReservationEntity reservation;

}
