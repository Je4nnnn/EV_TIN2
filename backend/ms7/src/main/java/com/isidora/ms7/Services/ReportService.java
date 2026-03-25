package com.isidora.ms7.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isidora.ms7.Entities.ReportEntity;
import com.isidora.ms7.Models.GroupSizeDscEntity;
import com.isidora.ms7.Models.ReservationEntity;
import com.isidora.ms7.Models.TariffEntity;
import com.isidora.ms7.Repositories.ReportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<ReservationEntity> getAllReservations() {
        ResponseEntity<List<ReservationEntity>> response = restTemplate.exchange(
                "http://ms5/api/reservations/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationEntity>>() {}
        );
        return response.getBody();
    }

    public List<TariffEntity> getAllTariffs() {
        ResponseEntity<List<TariffEntity>> response = restTemplate.exchange(
                "http://ms1/api/tariffs/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TariffEntity>>() {}
        );
        return response.getBody();
    }

    public List<GroupSizeDscEntity> getAllGroupSizeDsc() {
        ResponseEntity<List<GroupSizeDscEntity>> response = restTemplate.exchange(
                "http://ms2/api/groups/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GroupSizeDscEntity>>() {}
        );
        return response.getBody();
    }


    public List<ReportEntity> getAllReports() {
        List<ReportEntity> reports = reportRepository.findAll();
        if(reports.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron reportes");
        }
        return reports;
    }

    public ReportEntity generateAndSaveLapsTimeReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> reportData = generateLapsTimeReport(startDate, endDate);

        try {
            ReportEntity report = new ReportEntity();
            report.setReportType("lapsTime");
            report.setStartDate(startDate);
            report.setEndDate(endDate);
            report.setGrandTotal((Double) reportData.get("grandTotal"));
            report.setReportData(objectMapper.writeValueAsString(reportData));

            return reportRepository.save(report);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el reporte: " + e.getMessage());
        }
    }

    public Map<String, Object> generateLapsTimeReport(LocalDate startDate, LocalDate endDate) {
        List<ReservationEntity> reservations = getReservationsBetweenDates(startDate, endDate);
        List<TariffEntity> tariffs = getAllTariffs();

        List<Map<String, Object>> categories = tariffs.stream()
                .map(tariff -> {
                    Map<String, Object> category = new HashMap<>();
                    category.put("id", tariff.getId_tariff());
                    category.put("name", tariff.getLaps() + " vueltas / " + tariff.getMaxMinutes() + " minutos");
                    category.put("laps", tariff.getLaps());
                    category.put("minutes", tariff.getMaxMinutes());
                    return category;
                })
                .collect(Collectors.toList());

        List<YearMonth> months = getMonthsBetween(startDate, endDate);

        Map<Long, Map<YearMonth, Double>> tariffMonthlyRevenue = initializeRevenueStructureLaps(tariffs, months);
        processReservations(reservations, tariffs, tariffMonthlyRevenue);

        return generateFinalReport(categories, months, tariffMonthlyRevenue, startDate, endDate);
    }

    // ... (resto de metodos auxiliares igual que en la respuesta anterior)
    // Metodo para obtener reservas entre fechas
    private List<ReservationEntity> getReservationsBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Obtener todas las reservas usando el método existente
        List<ReservationEntity> allReservations = getAllReservations();

        // Filtrar las reservas que están dentro del rango de fechas
        return allReservations.stream()
                .filter(reservation -> {
                    LocalDate reservationDate = reservation.getDate();
                    return !reservationDate.isBefore(startDate) &&
                            !reservationDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }


    // Metodo para obtener meses entre dos fechas
    private List<YearMonth> getMonthsBetween(LocalDate startDate, LocalDate endDate) {
        List<YearMonth> months = new ArrayList<>();
        YearMonth start = YearMonth.from(startDate);
        YearMonth end = YearMonth.from(endDate);

        while (!start.isAfter(end)) {
            months.add(start);
            start = start.plusMonths(1);
        }
        return months;
    }

    // Metodo para inicializar la estructura de ingresos
    private Map<Long, Map<YearMonth, Double>> initializeRevenueStructureLaps(List<TariffEntity> tariffs,
                                                                         List<YearMonth> months) {
        Map<Long, Map<YearMonth, Double>> tariffMonthlyRevenue = new HashMap<>();
        for (TariffEntity tariff : tariffs) {
            Map<YearMonth, Double> monthlyRevenue = new HashMap<>();
            for (YearMonth month : months) {
                monthlyRevenue.put(month, 0.0);
            }
            tariffMonthlyRevenue.put(tariff.getId_tariff(), monthlyRevenue);
        }
        return tariffMonthlyRevenue;
    }

    // Metodo para procesar las reservas
    private void processReservations(List<ReservationEntity> reservations,
                                     List<TariffEntity> tariffs,
                                     Map<Long, Map<YearMonth, Double>> tariffMonthlyRevenue) {
        for (ReservationEntity reservation : reservations) {
            if (reservation.getBaseTariff() != null) {
                tariffs.stream()
                        .filter(t -> t.getPrice().equals(reservation.getBaseTariff()))
                        .findFirst()
                        .ifPresent(matchingTariff -> {
                            Long tariffId = matchingTariff.getId_tariff();
                            YearMonth month = YearMonth.from(reservation.getDate());
                            Map<YearMonth, Double> monthlyRevenue = tariffMonthlyRevenue.get(tariffId);
                            double currentRevenue = monthlyRevenue.getOrDefault(month, 0.0);
                            // Usar 0.0 como valor por defecto si totalAmount es null
                            double amount = reservation.getTotalAmount() != null ? reservation.getTotalAmount() : 0.0;
                            monthlyRevenue.put(month, currentRevenue + amount);
                        });
            }
        }
    }


    // Metodo para generar el reporte final
    private Map<String, Object> generateFinalReport(List<Map<String, Object>> categories,
                                                    List<YearMonth> months,
                                                    Map<Long, Map<YearMonth, Double>> tariffMonthlyRevenue,
                                                    LocalDate startDate,
                                                    LocalDate endDate) {
        double[] totalsByMonth = new double[months.size()];
        double grandTotal = 0.0;
        List<Map<String, Object>> reportData = new ArrayList<>();

        // Procesar datos por categoría
        for (Map<String, Object> category : categories) {
            Long tariffId = (Long) category.get("id");
            Map<YearMonth, Double> monthlyRevenue = tariffMonthlyRevenue.get(tariffId);

            double[] values = new double[months.size()];
            double categoryTotal = 0.0;

            for (int i = 0; i < months.size(); i++) {
                YearMonth month = months.get(i);
                double revenue = monthlyRevenue.getOrDefault(month, 0.0);
                values[i] = revenue;
                totalsByMonth[i] += revenue;
                categoryTotal += revenue;
                grandTotal += revenue;
            }

            Map<String, Object> categoryData = new HashMap<>(category);
            categoryData.put("values", values);
            categoryData.put("total", categoryTotal);
            reportData.add(categoryData);
        }

        // Formatear nombres de meses
        List<String> monthLabels = months.stream()
                .map(m -> m.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " " + m.getYear())
                .collect(Collectors.toList());

        // Construir resultado final
        Map<String, Object> result = new HashMap<>();
        result.put("reportType", "lapsTime");
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("categories", categories);
        result.put("months", monthLabels);
        result.put("data", reportData);
        result.put("totalsByMonth", totalsByMonth);
        result.put("grandTotal", grandTotal);

        // Guardar el reporte en la base de datos
        try {
            ReportEntity reportEntity = new ReportEntity();
            reportEntity.setReportType("lapsTime");
            reportEntity.setStartDate(startDate);
            reportEntity.setEndDate(endDate);
            reportEntity.setGrandTotal(grandTotal);
            reportEntity.setReportData(objectMapper.writeValueAsString(result));
            reportRepository.save(reportEntity);
        } catch (Exception e) {
            // Log el error pero permite que el método continúe
            System.err.println("Error al guardar el reporte: " + e.getMessage());
        }



        return result;
    }

    // generar reporte por tamaño de grupo
    public Map<String, Object> generateGroupSizeReport(LocalDate startDate, LocalDate endDate) {
        List<ReservationEntity> reservations = getReservationsBetweenDates(startDate, endDate);
        List<GroupSizeDscEntity> groupSizeRanges = getAllGroupSizeDsc();

        List<Map<String, Object>> categories = createGroupSizeCategories(groupSizeRanges);
        List<YearMonth> months = getMonthsBetween(startDate, endDate);

        Map<Long, Map<YearMonth, Double>> groupSizeMonthlyRevenue = initializeRevenueStructureGroups(groupSizeRanges, months);
        processReservationsByGroupSize(reservations, groupSizeRanges, groupSizeMonthlyRevenue);

        return generateFinalGroupSizeReport(categories, months, groupSizeMonthlyRevenue, startDate, endDate);
    }

    private Map<String, Object> generateFinalGroupSizeReport(
            List<Map<String, Object>> categories,
            List<YearMonth> months,
            Map<Long, Map<YearMonth, Double>> groupSizeMonthlyRevenue,
            LocalDate startDate,
            LocalDate endDate) {

        List<Map<String, Object>> reportData = new ArrayList<>();
        double[] totalsByMonth = new double[months.size()];
        double grandTotal = 0.0;

        // Procesar datos por categoría
        for (Map<String, Object> category : categories) {
            Long rangeId = (Long) category.get("id");
            Map<YearMonth, Double> monthlyRevenue = groupSizeMonthlyRevenue.get(rangeId);

            double[] values = new double[months.size()];
            double categoryTotal = 0.0;

            for (int i = 0; i < months.size(); i++) {
                YearMonth month = months.get(i);
                double revenue = monthlyRevenue.getOrDefault(month, 0.0);
                values[i] = revenue;
                totalsByMonth[i] += revenue;
                categoryTotal += revenue;
                grandTotal += revenue;
            }

            Map<String, Object> categoryData = new HashMap<>(category);
            categoryData.put("values", values);
            categoryData.put("total", categoryTotal);
            reportData.add(categoryData);
        }

        // Formatear nombres de meses en español
        List<String> monthLabels = months.stream()
                .map(m -> m.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " " + m.getYear())
                .collect(Collectors.toList());

        // Construir resultado final
        Map<String, Object> result = new HashMap<>();
        result.put("reportType", "groupSize");
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("categories", categories);
        result.put("months", monthLabels);
        result.put("data", reportData);
        result.put("totalsByMonth", totalsByMonth);
        result.put("grandTotal", grandTotal);

        try {
            ReportEntity report = new ReportEntity();
            report.setReportType("groupSize");
            report.setStartDate(startDate);
            report.setEndDate(endDate);
            report.setGrandTotal(grandTotal);
            report.setReportData(objectMapper.writeValueAsString(reportData));

            reportRepository.save(report);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el reporte: " + e.getMessage());
        }

        return result;
    }

    public ReportEntity generateAndSaveGroupSizeReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> reportData = generateGroupSizeReport(startDate, endDate);

        try {
            ReportEntity report = new ReportEntity();
            report.setReportType("groupSize");
            report.setStartDate(startDate);
            report.setEndDate(endDate);
            report.setGrandTotal((Double) reportData.get("grandTotal"));
            report.setReportData(objectMapper.writeValueAsString(reportData));

            return reportRepository.save(report);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el reporte: " + e.getMessage());
        }
    }

    private List<Map<String, Object>> createGroupSizeCategories(List<GroupSizeDscEntity> groupSizeRanges) {
        return groupSizeRanges.stream()
                .map(range -> {
                    Map<String, Object> category = new HashMap<>();
                    category.put("id", range.getIdGroupSizeDsc());
                    category.put("name", range.getMinGroupSize() + "-" + range.getMaxGroupSize() + " personas");
                    category.put("min", range.getMinGroupSize());
                    category.put("max", range.getMaxGroupSize());
                    return category;
                })
                .collect(Collectors.toList());
    }

    private Map<Long, Map<YearMonth, Double>> initializeRevenueStructureGroups(
            List<GroupSizeDscEntity> groupSizeRanges, List<YearMonth> months) {
        Map<Long, Map<YearMonth, Double>> groupSizeMonthlyRevenue = new HashMap<>();
        for (GroupSizeDscEntity range : groupSizeRanges) {
            Map<YearMonth, Double> monthlyRevenue = new HashMap<>();
            for (YearMonth month : months) {
                monthlyRevenue.put(month, 0.0);
            }
            groupSizeMonthlyRevenue.put(range.getIdGroupSizeDsc(), monthlyRevenue);
        }
        return groupSizeMonthlyRevenue;
    }

    private void processReservationsByGroupSize(
            List<ReservationEntity> reservations,
            List<GroupSizeDscEntity> groupSizeRanges,
            Map<Long, Map<YearMonth, Double>> groupSizeMonthlyRevenue) {

        for (ReservationEntity reservation : reservations) {
            if (reservation.getGroupSize() != null && reservation.getTotalAmount() != null) {
                groupSizeRanges.stream()
                        .filter(range -> isInRange(reservation.getGroupSize(), range))
                        .findFirst()
                        .ifPresent(range -> updateRevenue(reservation, range, groupSizeMonthlyRevenue));
            }
        }
    }

    private boolean isInRange(Integer groupSize, GroupSizeDscEntity range) {
        return groupSize >= range.getMinGroupSize() && groupSize <= range.getMaxGroupSize();
    }

    private void updateRevenue(
            ReservationEntity reservation,
            GroupSizeDscEntity range,
            Map<Long, Map<YearMonth, Double>> groupSizeMonthlyRevenue) {

        YearMonth month = YearMonth.from(reservation.getDate());
        Map<YearMonth, Double> monthlyRevenue = groupSizeMonthlyRevenue.get(range.getIdGroupSizeDsc());
        if (monthlyRevenue != null) {
            double currentRevenue = monthlyRevenue.getOrDefault(month, 0.0);
            monthlyRevenue.put(month, currentRevenue + reservation.getTotalAmount());
        }
    }


}


