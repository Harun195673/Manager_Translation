package Reentry.first.Controller;

import Reentry.first.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import Reentry.first.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import Reentry.first.Service.TaskAssignmentService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/TaskAssignment")
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
