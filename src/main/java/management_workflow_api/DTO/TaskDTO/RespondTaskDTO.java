package management_workflow_api.DTO.TaskDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class RespondTaskDTO {
    private Long id;
    private String title;
    private String message;
    private Long managerId;
    private LocalDate createdDateTask;
}
