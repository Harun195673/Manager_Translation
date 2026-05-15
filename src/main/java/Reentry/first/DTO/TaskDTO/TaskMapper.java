package Reentry.first.DTO.TaskDTO;

import Reentry.first.Entity.Task;
import Reentry.first.Entity.Manager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper {

    public Task toEntity(RequestTaskDTO dto, Manager manager){
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setMessage(dto.getMessage());
        task.setStatus(Task.Status.TODO);
        task.setManager(manager);
        return task;
    }

    public RespondTaskDTO toRespondDTO(Task task){
        RespondTaskDTO dto = new RespondTaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setMessage(task.getMessage());
        dto.setStatus(task.getStatus());
        dto.setManagerId(task.getManager() != null ? task.getManager().getId() : null);
        return dto;
    }

    public List<RespondTaskDTO> respondTaskDTOList (List<Task> taskList){

        List<RespondTaskDTO> respondTaskDTOList = new ArrayList<>();

        for (Task task: taskList){
            respondTaskDTOList.add(this.toRespondDTO(task));
        }

        return respondTaskDTOList;
    }





}