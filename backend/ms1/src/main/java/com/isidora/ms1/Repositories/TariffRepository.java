package com.isidora.ms1.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isidora.ms1.Entities.TariffEntity;

public interface TariffRepository extends JpaRepository<TariffEntity, Long> {

    TariffEntity findByMaxMinutes(Integer max_minutes);

    TariffEntity findByLaps(Integer laps);
}
