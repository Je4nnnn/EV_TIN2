package com.isidora.ms1.Services;

import java.time.LocalDate;
import java.util.List;

import com.isidora.ms1.Models.SpecialDayEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.isidora.ms1.Entities.TariffEntity;
import com.isidora.ms1.Repositories.TariffRepository;
import org.springframework.web.client.RestTemplate;

@Service
public class TariffService {
     @Autowired
    TariffRepository tariffRepository;
    @Autowired
    private RestTemplate restTemplate;

    //URL ms4 dias especiales
    public List<SpecialDayEntity> getAllSpecialDays() {
        ResponseEntity<List<SpecialDayEntity>> response = restTemplate.exchange(
                "http://ms4/api/special/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SpecialDayEntity>>() {}
        );
        return response.getBody();
    }

    public List<TariffEntity> getAllTariffs() {
        return tariffRepository.findAll();
    }

    public TariffEntity getTariffEntityByMax_minutes(Integer minutes) {
        TariffEntity tariffEntity = tariffRepository.findByMaxMinutes(minutes);
        System.out.println("esta es la tarifa : " + tariffEntity.getPrice());
        return tariffEntity;
    }
    // encontrar tarifa por numero de laps
    public TariffEntity getTariffEntityByLaps(Integer laps) {
        TariffEntity tariffEntity = tariffRepository.findByLaps(laps);
        System.out.println("esta es la tarifa : " + tariffEntity.getPrice());
        return tariffEntity;
    }
    // tarifas por fecha y vueltas
    public TariffEntity getTariffEntityByDate(LocalDate date, Integer laps) {
        TariffEntity tariffEntity = tariffRepository.findByLaps(laps);
        System.out.println("esta es la Tarifa Base: " + tariffEntity.getPrice());
        //buscar si es dia especial
        List<SpecialDayEntity> specialDays = getAllSpecialDays();
        Double percentage = 0.0;
        for (SpecialDayEntity specialDay : specialDays) {
            if(specialDay.getDate().equals(date)){
                percentage = specialDay.getSpecialDiscount();
                System.out.println("esta es el porcentaje de tarifa especial: " + specialDay.getSpecialDiscount());
                tariffEntity.setPrice((int) (tariffEntity.getPrice() * (percentage)));
                break;
            }
        }
        return tariffEntity;
    }

}
