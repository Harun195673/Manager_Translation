package Reentry.first.DTO.WorkgroupDTO;

import Reentry.first.Entity.Manager;
import Reentry.first.Entity.WorkGroup;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespondWorkGroupDTO {


    private Long id;
    private String name;
    private String managerName;

    public RespondWorkGroupDTO(WorkGroup workGroup){
        this.id = workGroup.getId();
        this.name = workGroup.getName();
        this.managerName = workGroup.getManager().getName();
    }



}
