package Reentry.first.DTO.EmployeeDTO;

import Reentry.first.Entity.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEmployeeDTO {

    private Long workGroupId;
    private String name;
    private Employee.Language language;

}
