package Reentry.first.DTO.EmployeeDTO;

import Reentry.first.Entity.Employee;
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
    @NotBlank(message = "Language required")
    private Employee.Language language;

}
