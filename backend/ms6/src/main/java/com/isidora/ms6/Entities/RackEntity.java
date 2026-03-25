package com.isidora.ms6.Entities;

import com.isidora.ms6.Models.ReservationEntity;
import jakarta.persistence.*;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Racks")
public class RackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRack;
    private LocalDate date;
    private Time startTime;
    private Time endTime;
    private Integer groupSize;
    private Long idReservation;
}
