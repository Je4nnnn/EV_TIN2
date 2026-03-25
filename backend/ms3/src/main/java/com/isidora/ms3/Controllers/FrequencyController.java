package com.isidora.ms3.Controllers;


import com.isidora.ms3.Entities.FrequencyDscEntity;
import com.isidora.ms3.Services.FrequencyDscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/frequents")

public class FrequencyController {
    @Autowired
    private FrequencyDscService frequencyService;

    @GetMapping("/all")
    public List<FrequencyDscEntity> getAllFrequencyDsc(){
        return frequencyService.getAllFrequencyDsc();
    }
    @GetMapping("/frequency")
    public Double getDiscountPercentage(@RequestParam Integer frequency){
        return frequencyService.getDiscountPercentage(frequency);
    }
}
