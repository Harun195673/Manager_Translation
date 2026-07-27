package ServiceTests.DTO.WorkgroupDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestWorkGroupDTO {

    @NotBlank(message = "Name required")
    private String name;
    @NotNull(message = "managerId required")
    private Long managerId;
}
