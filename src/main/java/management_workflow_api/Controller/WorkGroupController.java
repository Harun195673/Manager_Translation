package management_workflow_api.Controller;


import management_workflow_api.DTO.WorkgroupDTO.RequestWorkGroupDTO;
import management_workflow_api.DTO.WorkgroupDTO.RespondWorkGroupDTO;
import management_workflow_api.Service.WorkGroupService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/manager/workgroups")
public class WorkGroupController {

    private final WorkGroupService workGroupService;

    public WorkGroupController(WorkGroupService workGroupService) {
        this.workGroupService = workGroupService;
    }

    /// CREATE
    @PostMapping
    public RespondWorkGroupDTO createWorkGroup(@RequestBody @Valid RequestWorkGroupDTO dto) {
        return workGroupService.createWorkGroup(dto);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public RespondWorkGroupDTO getWorkGroupById(@PathVariable Long id) {
        return workGroupService.getWorkGroupById(id);
    }

    // GET ALL
    @GetMapping
    public List<RespondWorkGroupDTO> getAllWorkGroups() {
        return workGroupService.getAllWorkGroups();
    }

    // UPDATE
    @PutMapping("/{id}")
    public RespondWorkGroupDTO updateWorkGroup(@PathVariable Long id,
                                               @Valid @RequestBody RequestWorkGroupDTO dto) {
        return workGroupService.updateWorkGroup(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteWorkGroup(@PathVariable Long id) {
        workGroupService.deleteWorkGroup(id);
    }
}