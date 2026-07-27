package ServiceTests;

import ServiceTests.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import ServiceTests.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import ServiceTests.Entity.Employee;
import ServiceTests.Entity.Task;
import ServiceTests.Exceptions.InvalidOperationException;
import ServiceTests.Exceptions.ResourceNotFoundException;
import ServiceTests.Repository.EmployeeRepository;
import ServiceTests.Repository.TaskAssignmentRepository;
import ServiceTests.Repository.TaskRepository;
import ServiceTests.Service.TaskAssignmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskAssignmentServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TaskAssignmentRepository taskAssignmentRepository;

    @Mock
    private TaskAssignmentMapper taskAssignmentMapper;

    @InjectMocks
    private TaskAssignmentService taskAssignmentService;

    @Test
    void assignTaskToEmployee_shouldThrowResourceNotFoundException_whenTaskDoesNotExist() {
        RequestTaskAssignmentDTO dto = new RequestTaskAssignmentDTO();
        dto.setName("Assignment A");
        dto.setDeadline(LocalDate.now().plusDays(3));
        dto.setTaskId(1L);
        dto.setEmployeeId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskAssignmentService.assignTaskToEmployee(dto);
        });

        verify(employeeRepository, never()).findById(anyLong());
        verify(taskAssignmentMapper, never()).toEntity(any());
        verify(taskAssignmentRepository, never()).save(any());
    }

    @Test
    void assignTaskToEmployee_shouldThrowInvalidOperationException_whenDeadlineBeforeTaskCreatedDate() {
        RequestTaskAssignmentDTO dto = new RequestTaskAssignmentDTO();
        dto.setName("Assignment A");
        dto.setDeadline(LocalDate.now().plusDays(3));
        dto.setTaskId(1L);
        dto.setEmployeeId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setCreatedDateTask(LocalDate.now().plusDays(4));

        Employee employee = new Employee();
        employee.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(InvalidOperationException.class, () -> {
            taskAssignmentService.assignTaskToEmployee(dto);
        });

        verify(taskAssignmentMapper, never()).toEntity(any());
        verify(taskAssignmentRepository, never()).save(any());
    }
}