package ServiceTests.DTO.TaskDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTaskDTO {

    @NotBlank(message = "title required")
    private String title;
    @NotBlank(message = "message required")
    private String message;
    @NotNull(message = "managerId required")
    private Long managerId;
    @NotNull(message = "taskAssignmentId required")
    private Long taskAssignmentId;
}