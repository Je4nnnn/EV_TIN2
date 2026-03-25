package com.isidora.ms4.Services;


import com.isidora.ms4.Entities.SpecialDayEntity;
import com.isidora.ms4.Repositories.SpecialDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpecialDayService {
    @Autowired
    private SpecialDayRepository specialDayRepository;

    public List<SpecialDayEntity> getAllSpecialDays() {
        return specialDayRepository.findAll();
    }

    //calcular descuento por cumpleaños
    public List<Double> getBirthdayDsc(LocalDate reservationDate, List<LocalDate> birthdays) {
        List<Double> birthdayDsc = new ArrayList<>();
        int birthdayCount = 0;
        int maxDiscounts;

        // Determinar máximo de descuentos según tamaño del grupo
        if (birthdays.size() >= 3 && birthdays.size() <= 5) {
            maxDiscounts = 1;
        } else if (birthdays.size() >= 6 && birthdays.size() <= 10) {
            maxDiscounts = 2;
        } else if (birthdays.size() <= 3){
            maxDiscounts = 1;
            System.out.println("grupos de 1 a 2 un cumpleañero con descuento");
        }
        else {
            maxDiscounts = 0;
        }

        // Iterar sobre cada cumpleaños
        for (int i = 0; i < birthdays.size(); i++) {
            LocalDate birthday = birthdays.get(i);

            // Verificar si el día y mes coinciden
            if (birthday.getMonth() == reservationDate.getMonth() && birthday.getDayOfMonth() == reservationDate.getDayOfMonth() && birthdayCount < maxDiscounts) {
                birthdayDsc.add(50.0);
                birthdayCount++;
            } else {
                birthdayDsc.add(0.0);
            }
        }

        return birthdayDsc;
    }


}
