package ServiceTests.Service;

import ServiceTests.DTO.ManagerDTO.ManagerMapper;
import ServiceTests.DTO.ManagerDTO.RequestManagerDTO;
import ServiceTests.DTO.ManagerDTO.RespondManagerDTO;
import ServiceTests.DTO.WebUser.WebUserMapper;
import ServiceTests.Entity.Manager;
import ServiceTests.Entity.WebUser;
import ServiceTests.Exceptions.DuplicateResourceException;
import ServiceTests.Exceptions.InvalidOperationException;
import ServiceTests.Exceptions.ResourceNotFoundException;
import ServiceTests.Repository.ManagerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;
    private final WebUserMapper webUserMapper;
    private final PasswordEncoder passwordEncoder;

    public ManagerService(ManagerRepository managerRepository,
                          ManagerMapper managerMapper,
                          WebUserMapper webUserMapper,
                          PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
        this.webUserMapper = webUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /// CREATE
    @Transactional
    public RespondManagerDTO createManager(RequestManagerDTO dto) {

        validateManagerNameIsAvailable(dto.getName());

        Manager manager = managerMapper.toEntity(dto);

        WebUser webUser = webUserMapper.fromManager(dto);
        webUser.setPassword(passwordEncoder.encode(webUser.getPassword()));

        manager.setWebUser(webUser);
        webUser.setManager(manager);

        Manager savedManager = managerRepository.save(manager);

        return managerMapper.toRespondDTO(savedManager);
    }

    /// GET BY ID
    public RespondManagerDTO getManagerById(Long id) {

        Manager manager = findManagerById(id);

        return managerMapper.toRespondDTO(manager);
    }

    /// GET ALL
    public List<RespondManagerDTO> getAllManagers() {

        List<Manager> managers = managerRepository.findAll();

        return managerMapper.respondManagerDTOList(managers);
    }

    /// UPDATE
    @Transactional
    public RespondManagerDTO updateManager(Long id, RequestManagerDTO dto) {

        Manager manager = findManagerById(id);

        validateManagerNameForUpdate(manager, dto.getName());

        manager.setName(dto.getName());

        Manager savedManager = managerRepository.save(manager);

        return managerMapper.toRespondDTO(savedManager);
    }

    /// DELETE
    @Transactional
    public void deleteManager(Long id) {

        Manager manager = findManagerById(id);

        if (!manager.getWorkGroupList().isEmpty()) {
            throw new InvalidOperationException(
                    "Cannot delete Manager with existing workGroups"
            );
        }

        managerRepository.delete(manager);
    }

    private Manager findManagerById(Long id) {

        return managerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manager not found"));
    }

    private void validateManagerNameIsAvailable(String name) {

        if (managerRepository.existsByName(name)) {
            throw new DuplicateResourceException(
                    "Manager already exists. Choose a different name"
            );
        }
    }

    private void validateManagerNameForUpdate(Manager manager, String newName) {

        boolean nameChanged = !manager.getName().equals(newName);
        boolean nameAlreadyExists = managerRepository.existsByName(newName);

        if (nameChanged && nameAlreadyExists) {
            throw new DuplicateResourceException(
                    "Manager already exists. Choose a different name"
            );
        }
    }
}