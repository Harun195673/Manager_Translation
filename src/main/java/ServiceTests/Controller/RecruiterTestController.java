package management_workflow_api.Controller;

import management_workflow_api.DTO.ManagerDTO.RequestWorkFlowDTO;
import management_workflow_api.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import management_workflow_api.Service.ManagerWorkflowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/public")
public class RecruiterTestController {

    private final ManagerWorkflowService managerWorkflowService;


    public RecruiterTestController(ManagerWorkflowService managerWorkflowService) {
        this.managerWorkflowService = managerWorkflowService;
    }

    @PostMapping("/demo-workflow")
    public List<RespondTaskAssignmentDTO> testWorkFlow() {

        RequestWorkFlowDTO workFlowDto = new RequestWorkFlowDTO();

        workFlowDto.setManagerId(1L);
        workFlowDto.setWorkGroupId(1L);
        workFlowDto.setDeadline(LocalDate.of(2030, 6, 15));
        workFlowDto.setTaskId(1L);
        workFlowDto.setTaskAssignmentName("Wisch den Boden im Flur.");

        return managerWorkflowService.taskTranslationWorkflow(workFlowDto);
    }


}
