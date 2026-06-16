package management_workflow_api.DTO.EmployeeDTO;

import management_workflow_api.Entity.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEmployeeDTO {

    @NotNull(message = "WorkGroupId required")
    private Long workGroupId;
    @NotBlank(message = "name required")
    private String name;
    @NotNull(message = "Language required")
    private Employee.Language language;

}
