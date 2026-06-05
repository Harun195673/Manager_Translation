package Reentry.first.DTO.TaskAssignmentDTO;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestTaskAssignmentDTO {

    @NotNull(message = "deadline is required")
    @Future (message = "deadline must be in the future")
    private LocalDate deadline;
    @NotNull(message = "taskId is required")
    private Long taskId;
    @NotNull(message = "employeeId is required")
    private Long employeeId;
    @NotBlank(message = "TaskAssignment name required.")
    private String name;
}
