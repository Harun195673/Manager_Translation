package management_workflow_api.Controller;

import management_workflow_api.DTO.ManagerDTO.RequestManagerDTO;
import management_workflow_api.DTO.ManagerDTO.RespondManagerDTO;
import management_workflow_api.Service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/manager")
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






}