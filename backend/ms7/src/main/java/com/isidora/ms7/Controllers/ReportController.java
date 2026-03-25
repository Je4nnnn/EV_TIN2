package com.isidora.ms7.Controllers;


import com.isidora.ms7.Entities.ReportEntity;
import com.isidora.ms7.Services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reports")

public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("/all")
    public List<ReportEntity> getAllReports(){
        return reportService.getAllReports();
    }
    // ahhhhhh
    @GetMapping("/laps-time")
    public ResponseEntity<Map<String, Object>> generateLapsTimeReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.generateLapsTimeReport(startDate, endDate));
    }
    @GetMapping("/group-size")
    public ResponseEntity<Map<String, Object>> generateGroupSizeReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            return ResponseEntity.ok(reportService.generateGroupSizeReport(startDate, endDate));
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el reporte: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public String test() {
        return "MS7 Reports Service is working!";
    }



}
