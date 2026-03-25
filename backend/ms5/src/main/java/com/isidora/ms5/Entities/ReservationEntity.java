package com.isidora.ms5.Entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Reservation")
@NamedEntityGraph(
        name = "Reservation.withCustomersAndPrices",
        attributeNodes = {
                @NamedAttributeNode("customers"),
                @NamedAttributeNode("individualPrices")
        }
)
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long id_reservation;

    private LocalDate date;
    private Time startTime;
    private Time endTime;
    private Integer groupSize;
    private Integer baseTariff;
    private Double totalAmount;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Double> individualDscs;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Double> individualPrices;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CustomerEntity> customers;

}
