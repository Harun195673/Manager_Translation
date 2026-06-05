package Reentry.first.DTO.TaskDTO.TaskAssignmentDTO;

import Reentry.first.Entity.Task;
import Reentry.first.Entity.TaskAssignment;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestUpdateTaskAssignmentDTO {

    @NotNull(message = "oldTaskAssignmentId required")
    private Long oldTaskAssignmentId;

    @NotNull(message = "id required")
    private Long newTaskAssignmentId;

    @FutureOrPresent
    @NotNull(message = "deadline required")
    private LocalDate deadline;

    @NotNull(message = "hoursWorked required")
    private int hoursWorked;
    @NotNull(message = "Status required")
    private TaskAssignment.Status status;


    @NotNull(message = "taskId required")
    private Long taskId;
    @NotNull(message = "employeeId required")
    private Long employeeId;

    @NotBlank(message = "TaskAssignment name required.")
    private String name;
}
