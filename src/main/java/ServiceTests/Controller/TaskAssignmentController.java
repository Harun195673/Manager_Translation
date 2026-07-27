package ServiceTests.Controller;

import ServiceTests.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import ServiceTests.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import ServiceTests.Service.TaskAssignmentService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/taskAssignments")
public class TaskAssignmentController {

    private final TaskAssignmentService taskAssignmentService;

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
