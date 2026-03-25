package com.isidora.ms7.Models;

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
