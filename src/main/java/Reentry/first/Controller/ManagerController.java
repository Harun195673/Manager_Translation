package Reentry.first.Controller;

import Reentry.first.DTO.ManagerDTO.RequestManagerDTO;
import Reentry.first.DTO.ManagerDTO.RequestWorkFlowDTO;
import Reentry.first.DTO.ManagerDTO.RespondManagerDTO;
import Reentry.first.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import Reentry.first.Service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    /// CREATE
    @PostMapping
    public RespondManagerDTO createManager(@RequestBody @Valid RequestManagerDTO dto) {
        return managerService.createManager(dto);
    }

    /// GET BY ID
    @GetMapping("/{id}")
    public RespondManagerDTO getManagerById(@PathVariable Long id) {
        return managerService.getManagerById(id);
    }

    /// GET ALL
    @GetMapping
    public List<RespondManagerDTO> getAllManagers() {
        return managerService.getAllManagers();
    }

    /// UPDATE
    @PutMapping("/{id}")
    public RespondManagerDTO updateManager(@PathVariable Long id,
                                           @RequestBody @Valid RequestManagerDTO dto) {
        return managerService.updateManager(id, dto);
    }

    /// DELETE
    @DeleteMapping("/{id}")
    public void deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
    }




    @PostMapping("/translate_and_assign")
    public List<RespondTaskAssignmentDTO> executeTaskWorkflow(
            @Valid @RequestBody RequestWorkFlowDTO workFlowDto) {

        return managerService.taskTranslationWorkflow(workFlowDto);
    }


}