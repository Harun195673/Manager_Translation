package Reentry.first.Controller;

import Reentry.first.DTO.TaskDTO.RequestTaskDTO;
import Reentry.first.DTO.TaskDTO.RespondTaskDTO;
import Reentry.first.Service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public RespondTaskDTO createTask(@RequestBody RequestTaskDTO dto){
        return taskService.createTask(dto);
    }

    @GetMapping
    public List<RespondTaskDTO> getAllTasks(){
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public RespondTaskDTO getTask(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}