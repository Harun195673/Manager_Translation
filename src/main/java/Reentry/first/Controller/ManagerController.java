package Reentry.first.Controller;

import Reentry.first.DTO.ManagerDTO.RequestManagerDTO;
import Reentry.first.DTO.ManagerDTO.RespondManagerDTO;
import Reentry.first.Service.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    /// CREATE
    @PostMapping
    public RespondManagerDTO createManager(@RequestBody RequestManagerDTO dto) {
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
                                           @RequestBody RequestManagerDTO dto) {
        return managerService.updateManager(id, dto);
    }

    /// DELETE
    @DeleteMapping("/{id}")
    public void deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
    }
}