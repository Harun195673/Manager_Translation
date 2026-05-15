package Reentry.first.Controller;


import Reentry.first.DTO.WorkgroupDTO.RequestWorkGroupDTO;
import Reentry.first.DTO.WorkgroupDTO.RespondWorkGroupDTO;
import Reentry.first.Service.WorkGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workgroups")
public class WorkGroupController {

    private final WorkGroupService workGroupService;

    public WorkGroupController(WorkGroupService workGroupService) {
        this.workGroupService = workGroupService;
    }

    /// CREATE
    @PostMapping
    public RespondWorkGroupDTO createWorkGroup(@RequestBody RequestWorkGroupDTO dto) {
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
                                               @RequestBody RequestWorkGroupDTO dto) {
        return workGroupService.updateWorkGroup(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteWorkGroup(@PathVariable Long id) {
        workGroupService.deleteWorkGroup(id);
    }
}