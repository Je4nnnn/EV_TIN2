package com.isidora.ms3.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FrequencyDsc")
public class FrequencyDscEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdFrequencyDsc;
    private String frequencyType;
    private Integer minFrequency;
    private Integer maxFrequency;
    private Double discountPercentage;
}
