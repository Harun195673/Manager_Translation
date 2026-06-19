package management_workflow_api.Controller;

import management_workflow_api.DTO.TaskDTO.RequestTaskDTO;
import management_workflow_api.DTO.TaskDTO.RespondTaskDTO;
import management_workflow_api.Service.TaskService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/manager/tasks")
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
    public RespondTaskDTO getTask(@RequestBody RequestTaskDTO requestTaskDTO,
                                  @PathVariable Long taskId){
        return taskService.updateTask(requestTaskDTO, taskId);
    }




    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}