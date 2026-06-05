package Reentry.first.Controller;

import Reentry.first.DTO.TaskDTO.RequestTaskDTO;
import Reentry.first.DTO.TaskDTO.RespondTaskDTO;
import Reentry.first.Service.TaskService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public RespondTaskDTO createTask(@RequestBody @Valid RequestTaskDTO dto){
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

    @PutMapping()
    public RespondTaskDTO getTask(@RequestBody RequestTaskDTO requestTaskDTO){
        return taskService.updateTask(requestTaskDTO);
    }




    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}