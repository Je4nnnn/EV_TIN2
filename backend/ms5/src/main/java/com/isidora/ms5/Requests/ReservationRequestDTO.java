
package com.isidora.ms5.Requests;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import com.isidora.ms5.Entities.CustomerEntity;

import lombok.Data;

@Data
public class ReservationRequestDTO {
        private LocalDate date;
    private Time startTime;
    private Integer laps; // en vueltas
    private Integer groupSize;
    private List<CustomerEntity> customers;
}
