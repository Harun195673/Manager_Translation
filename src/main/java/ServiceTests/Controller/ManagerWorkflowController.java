package ServiceTests.Controller;

import jakarta.validation.Valid;
import ServiceTests.DTO.ManagerDTO.RequestWorkFlowDTO;
import ServiceTests.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import ServiceTests.Service.ManagerWorkflowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workflow")
public class ManagerWorkflowController {

    private final ManagerWorkflowService managerWorkflowService;

    public ManagerWorkflowController (ManagerWorkflowService managerWorkflowService){
        this.managerWorkflowService  = managerWorkflowService;
    }


    @PostMapping("/translate-and-assign")
    public List<RespondTaskAssignmentDTO> executeTaskWorkflow(
            @Valid @RequestBody RequestWorkFlowDTO workFlowDto) {

        return managerWorkflowService.taskTranslationWorkflow(workFlowDto);
    }
}
