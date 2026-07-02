package management_workflow_api.Controller;
import lombok.RequiredArgsConstructor;
import management_workflow_api.DTO.WebUser.WebUserResponseDTO;
import management_workflow_api.Entity.WebUser;
import management_workflow_api.Service.WebUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/webUsers")
@RequiredArgsConstructor
public class WebUserController {

    private final WebUserService webUserService;

    @GetMapping
    public List<WebUserResponseDTO> findAll() {
        return webUserService.findAll();
    }

    @GetMapping("/{id}")
    public WebUserResponseDTO findById(@PathVariable Long id) {
        return webUserService.findById(id);
    }

    @PostMapping
    public WebUserResponseDTO create(@RequestBody WebUser webUser) {
        return webUserService.create(webUser);
    }

    @PutMapping("/{id}")
    public WebUserResponseDTO update(
            @PathVariable Long id,
            @RequestBody WebUser webUser
    ) {
        return webUserService.update(id, webUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        webUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}