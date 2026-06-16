package management_workflow_api;


import management_workflow_api.DTO.WorkgroupDTO.RequestWorkGroupDTO;
import management_workflow_api.DTO.WorkgroupDTO.WorkGroupMapper;
import management_workflow_api.Entity.Employee;
import management_workflow_api.Entity.WorkGroup;
import management_workflow_api.Exceptions.DuplicateResourceException;
import management_workflow_api.Exceptions.InvalidOperationException;
import management_workflow_api.Exceptions.ResourceNotFoundException;
import management_workflow_api.Repository.ManagerRepository;
import management_workflow_api.Repository.WorkGroupRepository;
import management_workflow_api.Service.WorkGroupService;
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
class WorkGroupServiceTest {

    @Mock
    private WorkGroupRepository workGroupRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private WorkGroupMapper workGroupMapper;

    @InjectMocks
    private WorkGroupService workGroupService;

    @Test
    void createWorkGroup_shouldThrowDuplicateResourceException_whenNameAlreadyExists() {
        RequestWorkGroupDTO dto = new RequestWorkGroupDTO();
        dto.setName("Group A");
        dto.setManagerId(1L);

        when(workGroupRepository.existsByName("Group A")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            workGroupService.createWorkGroup(dto);
        });

        verify(workGroupRepository, never()).save(any());
    }

    @Test
    void createWorkGroup_shouldThrowResourceNotFoundException_whenManagerDoesNotExist() {
        RequestWorkGroupDTO dto = new RequestWorkGroupDTO();
        dto.setName("Group A");
        dto.setManagerId(1L);

        when(workGroupRepository.existsByName("Group A")).thenReturn(false);
        when(managerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            workGroupService.createWorkGroup(dto);
        });

        verify(workGroupRepository, never()).save(any());
    }

    @Test
    void deleteWorkGroup_shouldThrowInvalidOperationException_whenEmployeesExist() {
        WorkGroup workGroup = new WorkGroup();
        workGroup.setId(1L);
        workGroup.setName("Group A");
        workGroup.setEmployeeList(new ArrayList<>());
        workGroup.getEmployeeList().add(new Employee());

        when(workGroupRepository.findById(1L)).thenReturn(Optional.of(workGroup));

        assertThrows(InvalidOperationException.class, () -> {
            workGroupService.deleteWorkGroup(1L);
        });

        verify(workGroupRepository, never()).delete(any());
    }

    @Test
    void deleteWorkGroup_shouldDeleteWorkGroup_whenNoEmployeesExist() {
        WorkGroup workGroup = new WorkGroup();
        workGroup.setId(1L);
        workGroup.setName("Group A");
        workGroup.setEmployeeList(new ArrayList<>());

        when(workGroupRepository.findById(1L)).thenReturn(Optional.of(workGroup));

        workGroupService.deleteWorkGroup(1L);

        verify(workGroupRepository).delete(workGroup);
    }
}