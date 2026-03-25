package com.isidora.ms6.Controllers;


import com.isidora.ms6.Entities.RackEntity;
import com.isidora.ms6.Models.ReservationEntity;
import com.isidora.ms6.Services.RackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/racks")

public class RackController {
    @Autowired
    RackService rackService;

    @GetMapping("/all")
    public List<RackEntity> getAllRacks(){
        rackService.updateRack();
        return rackService.getAllRacks();
    }

    @PostMapping("/create")
    public RackEntity createRacks(ReservationEntity reservation){
        return rackService.createRacks(reservation);
    }

    @PostMapping("/update")
    public void updateRack(){
        rackService.updateRack();
    }



}
