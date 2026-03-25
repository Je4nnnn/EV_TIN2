package com.isidora.ms5.Services;


import com.isidora.ms5.Entities.CustomerEntity;
import com.isidora.ms5.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Integer getFrequencyByRut(String rut){
        List<CustomerEntity> customers = customerRepository.findByRut(rut);
        return customers.size();
    }
}
