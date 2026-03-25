package com.isidora.ms5.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupSizeDscEntity {
    private Long idGroupSizeDsc;
    private Integer minGroupSize;
    private Integer maxGroupSize;
    private Double discountPercentage;


}
