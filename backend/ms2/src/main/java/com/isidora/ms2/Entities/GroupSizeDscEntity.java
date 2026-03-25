package com.isidora.ms2.Entities;

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
@Table(name = "GroupSizeDsc")
public class GroupSizeDscEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGroupSizeDsc;
    private Integer minGroupSize;
    private Integer maxGroupSize;
    private Double discountPercentage;

}
