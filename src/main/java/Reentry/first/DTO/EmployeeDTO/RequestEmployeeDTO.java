package Reentry.first.DTO.ManagerDTO.EmployeeDTO;

import Reentry.first.Entity.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEmployeeDTO {

    @NotNull
    private Long workGroupId;
    @NotBlank
    private String name;
    @NotBlank
    private Employee.Language language;

}
