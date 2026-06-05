package Reentry.first.DTO.TaskDTO.TaskAssignmentDTO;

import Reentry.first.Entity.Task;
import Reentry.first.Entity.TaskAssignment;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RespondTaskAssignmentDTO {

    private Long id;
    private LocalDate createdDate;
    private LocalDate deadline;
    private int hoursWorked;

    private TaskAssignment.Status status;


    private String taskTitle;
    private String employeeName;
    private String name;




}
