package Reentry.first.Controller;

import Reentry.first.DTO.ManagerDTO.RequestWorkFlowDTO;
import Reentry.first.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import Reentry.first.Entity.Manager;
import Reentry.first.Entity.Task;
import Reentry.first.Entity.WorkGroup;
import Reentry.first.Repository.ManagerRepository;
import Reentry.first.Service.ManagerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/recruiter")
public class RecruiterTestController {

    private final ManagerService managerService;


    public RecruiterTestController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping("/recruiterTest")
    public List<RespondTaskAssignmentDTO> testWorkFlow() {

        RequestWorkFlowDTO workFlowDto = new RequestWorkFlowDTO();

        workFlowDto.setManagerId(1L);
        workFlowDto.setWorkGroupId(1L);
        workFlowDto.setDeadline(LocalDate.of(2030, 6, 15));
        workFlowDto.setTaskId(1L);
        workFlowDto.setTaskAssignmentName("Clean the floors in the hallway");

        return managerService.taskTranslationWorkflow(workFlowDto);
    }


}
