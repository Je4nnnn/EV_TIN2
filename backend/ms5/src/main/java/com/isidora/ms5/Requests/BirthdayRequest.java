package com.isidora.ms5.Requests;

import lombok.Data;

import java.util.List;

@Data
public class BirthdayRequest {
    private String reservationDate;
    private List<String> birthdays;
}