package management_workflow_api.DTO.ManagerDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestManagerDTO {

    @NotBlank(message = "Name required")
    private String name;
}
