package com.isidora.ms5.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffEntity {
    private Long id_tariff;
    private Integer laps;
    private Integer maxMinutes;
    private Integer price;
    private Integer total_duration;
}
