package com.isidora.ms5.Repositories;
import com.isidora.ms5.Entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {


    CustomerEntity getCustomerEntitiesByRutEquals(String rut);

    List<CustomerEntity> findByRut(String rut);


}