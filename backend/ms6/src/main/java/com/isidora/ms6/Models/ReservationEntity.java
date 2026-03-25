package com.isidora.ms6.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEntity {
    private Long id_reservation;

    private LocalDate date;
    private Time startTime;
    private Time endTime;
    private Integer groupSize;
    private Integer baseTariff;
    private Double totalAmount;
    private List<Double> individualDscs;
    private List<Double> individualPrices;
    private List<CustomerEntity> customers;

}
