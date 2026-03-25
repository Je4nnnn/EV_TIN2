package com.isidora.ms7.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Reports")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReport;
    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double grandTotal;
    @Column(columnDefinition = "TEXT")
    private String reportData;  // Almacenará el JSON del reporte


}
