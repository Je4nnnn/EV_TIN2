package com.isidora.ms2.Controllers;


import com.isidora.ms2.Entities.GroupSizeDscEntity;
import com.isidora.ms2.Services.GroupSizeDscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/groups")

public class GroupSizeDscController {
    @Autowired
    GroupSizeDscService groupSizeDscService;


    @GetMapping("/all")
    public List<GroupSizeDscEntity> getAllGroupSizeDsc(){
        return groupSizeDscService.getAllGroupSizeDsc();
    }
    @GetMapping("/bySize")
    public GroupSizeDscEntity getGroupSizeDscBySize(@RequestParam int size){
        return groupSizeDscService.getGroupSizeDscBySize(size);
    }
}
