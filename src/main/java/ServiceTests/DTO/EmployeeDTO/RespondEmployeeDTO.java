package ServiceTests.DTO.EmployeeDTO;

import ServiceTests.Entity.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespondEmployeeDTO {


    private String name;
    private Employee.Language language;
    private Long employeeId;
    private Long workGroupId;
    private String workGroupName;

}
