package Reentry.first.DTO.WorkgroupDTO;

import Reentry.first.DTO.ManagerDTO.RequestManagerDTO;
import Reentry.first.DTO.ManagerDTO.RespondManagerDTO;
import Reentry.first.Entity.Manager;
import Reentry.first.Entity.WorkGroup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkGroupMapper {


    public WorkGroup toEntity (RequestWorkGroupDTO dto){
        WorkGroup workGroup = new WorkGroup(dto);
        return workGroup;
    }

    public WorkgroupRespondDTO toRespondDTO (WorkGroup workGroup){
        WorkgroupRespondDTO respondDTO = new WorkgroupRespondDTO(workGroup);
        return respondDTO;
    }

    public List<WorkgroupRespondDTO> requestWorkgroupRespondDTOList (List<WorkGroup> workGroupList){

        List<WorkgroupRespondDTO> workgroupRespondDTOList = new ArrayList<>();
        for (WorkGroup workGroup: workGroupList){
            workgroupRespondDTOList.add(this.toRespondDTO(workGroup));
        }
        return workgroupRespondDTOList;
    }



}
