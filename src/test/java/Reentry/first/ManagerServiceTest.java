package Reentry.first;

import Reentry.first.DTO.EmployeeDTO.EmployeeMapper;
import Reentry.first.DTO.ManagerDTO.ManagerMapper;
import Reentry.first.DTO.ManagerDTO.RequestManagerDTO;
import Reentry.first.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import Reentry.first.DTO.TaskDTO.TaskMapper;
import Reentry.first.Entity.Manager;
import Reentry.first.Exceptions.DuplicateResourceException;
import Reentry.first.Exceptions.InvalidOperationException;
import Reentry.first.Exceptions.ResourceNotFoundException;
import Reentry.first.Repository.*;
import Reentry.first.Service.ManagerService;
import Reentry.first.Service.TaskAssignmentService;
import Reentry.first.Service.TranslationService;
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
        manager.getWorkGroupList().add(new Reentry.first.Entity.WorkGroup());

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