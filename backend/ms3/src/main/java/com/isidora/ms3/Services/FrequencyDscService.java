package com.isidora.ms3.Services;

import com.isidora.ms3.Entities.FrequencyDscEntity;
import com.isidora.ms3.Repositories.FrequencyDscRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Service
public class FrequencyDscService {

    @Autowired
    private FrequencyDscRepository frequencyDscRepository;

    public List<FrequencyDscEntity> getAllFrequencyDsc(){
        return frequencyDscRepository.findAll();
    }

    public Double getDiscountPercentage(Integer frequency){
        List<FrequencyDscEntity> frequencyDscs = frequencyDscRepository.findAll();
        for(FrequencyDscEntity frequencyDsc : frequencyDscs){
            if(frequencyDsc.getMinFrequency() <= frequency && frequency <= frequencyDsc.getMaxFrequency()){
                return frequencyDsc.getDiscountPercentage();
            }
        }
        return 0.0;
    }


}
