package Reentry.first.DTO.WorkgroupDTO;

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

    public RespondWorkGroupDTO toRespondDTO (WorkGroup workGroup){
        RespondWorkGroupDTO respondDTO = new RespondWorkGroupDTO(workGroup);
        return respondDTO;
    }

    public List<RespondWorkGroupDTO> requestWorkgroupRespondDTOList (List<WorkGroup> workGroupList){

        List<RespondWorkGroupDTO> respondWorkGroupDTOList = new ArrayList<>();
        for (WorkGroup workGroup: workGroupList){
            respondWorkGroupDTOList.add(this.toRespondDTO(workGroup));
        }
        return respondWorkGroupDTOList;
    }



}
