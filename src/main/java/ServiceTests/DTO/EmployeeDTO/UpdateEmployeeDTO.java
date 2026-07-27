package ServiceTests.DTO.EmployeeDTO;

import ServiceTests.Entity.Employee;
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
