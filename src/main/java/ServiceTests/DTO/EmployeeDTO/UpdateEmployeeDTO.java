package management_workflow_api.DTO.EmployeeDTO;

import management_workflow_api.Entity.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmployeeDTO {

    private Long employeeId;
    private String name;
    private Employee.Language language;
    private Long workGroupId;
    private Long taskAssignmentId;

}
