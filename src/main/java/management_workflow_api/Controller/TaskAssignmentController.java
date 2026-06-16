package management_workflow_api.Controller;

import management_workflow_api.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import management_workflow_api.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import management_workflow_api.Service.TaskAssignmentService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/task-assignments")
public class TaskAssignmentController {

    TaskAssignmentService taskAssignmentService;

    public TaskAssignmentController(TaskAssignmentService taskAssignmentService) {
        this.taskAssignmentService = taskAssignmentService;
    }

    @PostMapping
    public RespondTaskAssignmentDTO assignTaskToEmployee (@RequestBody @Valid RequestTaskAssignmentDTO dto){
        return taskAssignmentService.assignTaskToEmployee(dto);
    }


    @GetMapping
    public List<RespondTaskAssignmentDTO> getAllTaskAssignments (){
        return taskAssignmentService.getAllTaskAssignments();
    }







}
