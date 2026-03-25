package com.isidora.ms5.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyDscEntity {
    private Long IdFrequencyDsc;
    private String frequencyType;
    private Integer minFrequency;
    private Integer maxFrequency;
    private Double discountPercentage;

}
