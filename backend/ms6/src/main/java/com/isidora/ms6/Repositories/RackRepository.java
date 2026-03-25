package com.isidora.ms6.Repositories;

import com.isidora.ms6.Entities.RackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RackRepository extends JpaRepository<RackEntity, Long> {
    boolean existsByIdReservation(Long idReservation);


}
