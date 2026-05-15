package Reentry.first.DTO.TaskDTO;

import lombok.Getter;
import lombok.Setter;
import Reentry.first.Entity.Task.Status;

@Getter
@Setter
public class RespondTaskDTO {
    private Long id;
    private String title;
    private String message;
    private Status status;
    private Long managerId;
}
