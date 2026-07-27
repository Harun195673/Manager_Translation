package management_workflow_api.Service;

import lombok.RequiredArgsConstructor;
import management_workflow_api.DTO.WebUser.WebUserMapper;
import management_workflow_api.DTO.WebUser.WebUserResponseDTO;
import management_workflow_api.Entity.WebUser;
import management_workflow_api.Repository.WebUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebUserService {

    private final WebUserRepository webUserRepository;
    private final WebUserMapper webUserMapper;

    public List<WebUserResponseDTO> findAll() {
        return webUserRepository.findAll()
                .stream()
                .map(webUserMapper::toResponseDto)
                .toList();
    }



    public WebUserResponseDTO findById(Long id) {
        WebUser webUser = webUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WebUser not found"));

        return webUserMapper.toResponseDto(webUser);
    }




    public WebUserResponseDTO create(WebUser webUser) {
        if (webUserRepository.existsByUsername(webUser.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        WebUser savedWebUser = webUserRepository.save(webUser);

        return webUserMapper.toResponseDto(savedWebUser);
    }




    public WebUserResponseDTO update(Long id, WebUser updatedWebUser) {
        WebUser existingWebUser = webUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WebUser not found"));

        existingWebUser.setUsername(updatedWebUser.getUsername());
        existingWebUser.setRole(updatedWebUser.getRole());

        WebUser savedWebUser = webUserRepository.save(existingWebUser);

        return webUserMapper.toResponseDto(savedWebUser);
    }



    public void delete(Long id) {
        if (!webUserRepository.existsById(id)) {
            throw new RuntimeException("WebUser not found");
        }

        webUserRepository.deleteById(id);
    }
}