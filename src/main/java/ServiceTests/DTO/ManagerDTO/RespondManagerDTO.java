package management_workflow_api.DTO.ManagerDTO;

import management_workflow_api.Entity.Manager;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RespondManagerDTO {

    private Long id;
    private String name;

    public RespondManagerDTO(Manager manager){

        this.id = manager.getId();
        this.name = manager.getName();
    }



}
