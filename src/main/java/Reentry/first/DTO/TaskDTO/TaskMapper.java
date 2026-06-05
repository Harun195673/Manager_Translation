package Reentry.first.DTO.TaskDTO;

import Reentry.first.Entity.Task;
import Reentry.first.Entity.Manager;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper {

    public Task toEntity(RequestTaskDTO dto, Manager manager){
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setMessage(dto.getMessage());
        task.setManager(manager);
        task.setCreatedDateTask(LocalDate.now());
        return task;
    }

    public RespondTaskDTO toRespondDTO(Task task){
        RespondTaskDTO dto = new RespondTaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setMessage(task.getMessage());
        dto.setManagerId(task.getManager() != null ? task.getManager().getId() : null);
        dto.setCreatedDateTask(task.getCreatedDateTask());
        return dto;
    }

    public List<RespondTaskDTO> respondTaskDTOList (List<Task> taskList){

        List<RespondTaskDTO> respondTaskDTOList = new ArrayList<>();

        for (Task task: taskList){
            respondTaskDTOList.add(this.toRespondDTO(task));
        }

        return respondTaskDTOList;
    }




    public Task createTranslatedTask(Task originalTask, String translatedMessage) {

        Task newTask = new Task();

        newTask.setManager(originalTask.getManager());
        newTask.setTitle(originalTask.getTitle());
        newTask.setMessage(translatedMessage);
        newTask.setTaskAssignmentList(
                new ArrayList<>(originalTask.getTaskAssignmentList())
        );



        return newTask;
    }





}