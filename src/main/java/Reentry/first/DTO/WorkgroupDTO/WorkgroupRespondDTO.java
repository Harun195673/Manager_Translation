package Reentry.first.DTO.WorkgroupDTO;

import Reentry.first.Entity.Manager;
import Reentry.first.Entity.WorkGroup;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkgroupRespondDTO {


    private Long id;
    private String name;
    private Manager manager;

    public WorkgroupRespondDTO (WorkGroup workGroup){
        this.id = workGroup.getId();
        this.name = workGroup.getName();
        this.manager = workGroup.getManager();
    }



}
