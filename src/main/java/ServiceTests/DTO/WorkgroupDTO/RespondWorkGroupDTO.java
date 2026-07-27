package management_workflow_api.DTO.WorkgroupDTO;

import management_workflow_api.Entity.WorkGroup;
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
