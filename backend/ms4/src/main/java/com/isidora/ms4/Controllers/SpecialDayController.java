package com.isidora.ms4.Controllers;


import com.isidora.ms4.Entities.SpecialDayEntity;
import com.isidora.ms4.Requests.BirthdayRequest;
import com.isidora.ms4.Services.SpecialDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/special")

public class SpecialDayController {

    @Autowired
    private SpecialDayService specialDayService;

    @GetMapping("/all")
    public List<SpecialDayEntity> getAllSpecialDays() {
        return specialDayService.getAllSpecialDays();
    }

    @PostMapping("/dsc")
    public List<Double> getBirthdayDsc(
            @RequestParam String reservationDate,
            @RequestParam List<String> birthdays) {
        return specialDayService.getBirthdayDsc(
                LocalDate.parse(reservationDate),
                birthdays.stream()
                        .map(LocalDate::parse)
                        .toList()
        );
    }
    /// vadsvadvasv


}
