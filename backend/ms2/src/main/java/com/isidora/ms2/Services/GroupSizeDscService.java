package com.isidora.ms2.Services;


import com.isidora.ms2.Entities.GroupSizeDscEntity;
import com.isidora.ms2.Repositories.GroupSizeDscRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupSizeDscService {
    @Autowired
    GroupSizeDscRepository groupSizeDscRepository;

    public List<GroupSizeDscEntity> getAllGroupSizeDsc(){
        return groupSizeDscRepository.findAll();
    }

    public GroupSizeDscEntity getGroupSizeDscBySize(int size) {
        List<GroupSizeDscEntity> groupSizeDscEntities = groupSizeDscRepository.findAll();
        for (GroupSizeDscEntity groupSizeDscEntity : groupSizeDscEntities) {
            if (size >= groupSizeDscEntity.getMinGroupSize() &&
                    size <= groupSizeDscEntity.getMaxGroupSize()) {
                return groupSizeDscEntity;
            }
        }
        // Si no, retorna el menor
        return groupSizeDscEntities.get(0);
    }
}
