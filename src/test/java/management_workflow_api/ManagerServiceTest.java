package management_workflow_api;

import management_workflow_api.DTO.EmployeeDTO.EmployeeMapper;
import management_workflow_api.DTO.ManagerDTO.ManagerMapper;
import management_workflow_api.DTO.ManagerDTO.RequestManagerDTO;
import management_workflow_api.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import management_workflow_api.DTO.TaskDTO.TaskMapper;
import management_workflow_api.Entity.Manager;
import management_workflow_api.Entity.WorkGroup;
import management_workflow_api.Exceptions.DuplicateResourceException;
import management_workflow_api.Exceptions.InvalidOperationException;
import management_workflow_api.Exceptions.ResourceNotFoundException;
import management_workflow_api.Repository.*;
import management_workflow_api.Service.ManagerService;
import management_workflow_api.Service.TaskAssignmentService;
import management_workflow_api.Service.TranslationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private WorkGroupRepository workGroupRepository;

    @Mock
    private ManagerMapper managerMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private TaskAssignmentRepository taskAssignmentRepository;

    @Mock
    private TaskAssignmentService taskAssignmentService;

    @Mock
    private TaskAssignmentMapper taskAssignmentMapper;

    @Mock
    private TranslationService translationService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private ManagerService managerService;

    @Test
    void createManager_shouldThrowDuplicateResourceException_whenManagerNameAlreadyExists() {
        RequestManagerDTO dto = new RequestManagerDTO();
        dto.setName("Alice");

        when(managerRepository.existsByName("Alice")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            managerService.createManager(dto);
        });

        verify(managerRepository, never()).save(any());
    }

    @Test
    void getManagerById_shouldThrowResourceNotFoundException_whenManagerDoesNotExist() {
        when(managerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            managerService.getManagerById(1L);
        });
    }

    @Test
    void deleteManager_shouldThrowInvalidOperationException_whenManagerHasWorkGroups() {
        Manager manager = new Manager();
        manager.setId(1L);
        manager.setName("Alice");
        manager.setWorkGroupList(new ArrayList<>());
        manager.getWorkGroupList().add(new WorkGroup());

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        assertThrows(InvalidOperationException.class, () -> {
            managerService.deleteManager(1L);
        });

        verify(managerRepository, never()).delete(any());
    }

    @Test
    void deleteManager_shouldDeleteManager_whenManagerHasNoWorkGroups() {
        Manager manager = new Manager();
        manager.setId(1L);
        manager.setName("Alice");
        manager.setWorkGroupList(new ArrayList<>());

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        managerService.deleteManager(1L);

        verify(managerRepository).delete(manager);
    }
}