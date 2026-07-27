package ServiceTests.DTO.ManagerDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestManagerDTO {

    @NotBlank(message = "Name required")
    private String name;
    @NotBlank(message = "userName required")
    private String userName;
    @NotBlank(message = "password required")
    private String password;
}
