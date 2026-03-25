package com.isidora.ms5.Repositories;

import com.isidora.ms5.Entities.ReservationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<ReservationEntity,Long> {

    List<ReservationEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<ReservationEntity> findAll();

    @EntityGraph(value = "Reservation.withCustomersAndPrices")
    Optional<ReservationEntity> findById(Long id);

}
