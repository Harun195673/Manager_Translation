package Reentry.first;

import Reentry.first.DTO.EmployeeDTO.EmployeeMapper;
import Reentry.first.Exceptions.DuplicateResourceException;
import Reentry.first.Exceptions.ResourceNotFoundException;
import Reentry.first.Service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import Reentry.first.DTO.EmployeeDTO.RequestEmployeeDTO;
import Reentry.first.Entity.Employee;
import Reentry.first.Repository.EmployeeRepository;
import Reentry.first.Repository.TaskAssignmentRepository;
import Reentry.first.Repository.WorkGroupRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private WorkGroupRepository workGroupRepository;

    @Mock
    private TaskAssignmentRepository taskAssignmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void createEmployee_shouldThrowDuplicateResourceException_whenEmployeeAlreadyExists() {
        RequestEmployeeDTO dto = new RequestEmployeeDTO();
        dto.setName("Mehmet");
        dto.setLanguage(Employee.Language.Turkish);
        dto.setWorkGroupId(1L);

        when(employeeRepository.existsByNameAndLanguage("Mehmet", Employee.Language.Turkish))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            employeeService.createEmployee(dto);
        });

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void createEmployee_shouldThrowResourceNotFoundException_whenWorkGroupDoesNotExist() {
        RequestEmployeeDTO dto = new RequestEmployeeDTO();
        dto.setName("Mehmet");
        dto.setLanguage(Employee.Language.Turkish);
        dto.setWorkGroupId(1L);

        when(employeeRepository.existsByNameAndLanguage("Mehmet", Employee.Language.Turkish))
                .thenReturn(false);

        when(workGroupRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.createEmployee(dto);
        });

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getEmployeeById_shouldThrowResourceNotFoundException_whenEmployeeDoesNotExist() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(1L);
        });
    }

    @Test
    void deleteEmployee_shouldDeleteEmployee_whenEmployeeExists() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Mehmet");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(employee);
    }
}