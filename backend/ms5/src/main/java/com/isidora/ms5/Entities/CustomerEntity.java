package com.isidora.ms5.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_customer;

    private String name;
    private String lastname;
    private String rut;
    private String email;
    private String phone;
    private LocalDate birthdate;
    private LocalDate visitDate;
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    @JsonBackReference
    private ReservationEntity reservation;


}
