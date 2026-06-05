package Reentry.first.DTO.EmployeeDTO;

import Reentry.first.Entity.Employee;
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
