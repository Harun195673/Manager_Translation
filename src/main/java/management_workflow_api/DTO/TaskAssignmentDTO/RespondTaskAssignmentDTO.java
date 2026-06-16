package management_workflow_api.DTO.TaskAssignmentDTO;

import management_workflow_api.Entity.TaskAssignment;
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
