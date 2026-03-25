package com.isidora.ms1.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isidora.ms1.Entities.TariffEntity;
import com.isidora.ms1.Services.TariffService;

@RestController
@RequestMapping("/api/tariffs")

public class TariffController {
    @Autowired
    TariffService tariffService;

    @GetMapping("/all")
    public List<TariffEntity> getAllTariffs() {
        return tariffService.getAllTariffs();
    }

    @GetMapping("/byMinute")
    public TariffEntity getTariffEntityByMinutes(@RequestParam Integer minutes) {
        return tariffService.getTariffEntityByMax_minutes(minutes);
    }

    //Encontrar tarifa por numero de laps
    @GetMapping("/byLaps")
    public TariffEntity getTariffEntityByLaps(@RequestParam Integer laps) {
        return tariffService.getTariffEntityByLaps(laps);
    }
    //Encontrar tarifa por fecha y numero de vueltas
    @GetMapping("/byDate")
    public TariffEntity getTariffEntityByDate(@RequestParam LocalDate date, @RequestParam Integer laps){
        return tariffService.getTariffEntityByDate(date, laps);
    }
    @GetMapping("/test")
    public String test() {
        return "MS1 Tariff Service is working!";
    }

    

}
