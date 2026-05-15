package Reentry.first.DTO.TaskDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTaskDTO {
    private String title;
    private String message;
    private Long managerId; // optional if you want to assign a manager
}