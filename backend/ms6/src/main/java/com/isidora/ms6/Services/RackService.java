package com.isidora.ms6.Services;


import com.isidora.ms6.Config.RestTemplateConfig;
import com.isidora.ms6.Entities.RackEntity;
import com.isidora.ms6.Models.ReservationEntity;
import com.isidora.ms6.Repositories.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RackService {
    @Autowired
    RackRepository rackRepository;
    @Autowired
    private RestTemplate restTemplate;
    //URL reservas
    public List<ReservationEntity> getAllReservations(){
        ResponseEntity<List<ReservationEntity>> response = restTemplate.exchange(
                "http://ms5/api/reservations/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationEntity>>() {}
        );
        return response.getBody();
    }


    public void updateRack(){
        List<ReservationEntity> reservations = getAllReservations();

        for(ReservationEntity reservation : reservations) {
            // Verifica si ya existe un rack con ese idReservation
            if (!rackRepository.existsByIdReservation(reservation.getId_reservation())) {
                createRacks(reservation);
                System.out.println("Rack creado");
            }
        }
        System.out.println("Rack actualizado");

    }


    public List<RackEntity> getAllRacks(){
        return rackRepository.findAll();
    }

    public RackEntity createRacks(ReservationEntity reservation){
        RackEntity rackEntity = new RackEntity();
        rackEntity.setDate(reservation.getDate());
        rackEntity.setStartTime(reservation.getStartTime());
        rackEntity.setEndTime(reservation.getEndTime());
        rackEntity.setGroupSize(reservation.getGroupSize());
        rackEntity.setIdReservation(reservation.getId_reservation());
        return rackRepository.save(rackEntity);
    }




}
