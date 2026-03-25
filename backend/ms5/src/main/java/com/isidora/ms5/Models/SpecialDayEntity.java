package com.isidora.ms5.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialDayEntity {
    private Long idSpecialDay;
    private String name;
    private LocalDate date;
    private Double specialDiscount;
}
