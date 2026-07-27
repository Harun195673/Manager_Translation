package ServiceTests.DTO.EmployeeDTO;

import ServiceTests.Entity.Employee;
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
    @NotBlank(message = "userName required")
    private String userName;
    @NotBlank(message = "password required")
    private String password;

}
