package Reentry.first.DTO.ManagerDTO;

import Reentry.first.Entity.Manager;
import Reentry.first.Entity.WorkGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RespondManagerDTO {

    private Long id;
    private String name;
    private List<WorkGroup> workGroupList;


    public RespondManagerDTO(Manager manager){

        this.id = manager.getId();
        this.name = manager.getName();
        this.workGroupList = manager.getWorkGroupList();
    }



}
