package management_workflow_api;

import management_workflow_api.DTO.EmployeeDTO.EmployeeMapper;
import management_workflow_api.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import management_workflow_api.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import management_workflow_api.DTO.TaskDTO.TaskMapper;
import management_workflow_api.Entity.Employee;
import management_workflow_api.Entity.Task;
import management_workflow_api.Entity.TaskAssignment;
import management_workflow_api.Exceptions.InvalidOperationException;
import management_workflow_api.Exceptions.ResourceNotFoundException;
import management_workflow_api.Repository.EmployeeRepository;
import management_workflow_api.Repository.TaskAssignmentRepository;
import management_workflow_api.Repository.TaskRepository;
import management_workflow_api.Service.TaskAssignmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskAssignmentServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

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

        TaskAssignment taskAssignment = new TaskAssignment();
        taskAssignment.setDeadline(dto.getDeadline());


        when(taskAssignmentMapper.toEntity(dto)).thenReturn(taskAssignment);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskAssignmentService.assignTaskToEmployee(dto);
        });

        verify(taskAssignmentRepository, never()).save(any());
    }











    @Test
    void assignTaskToEmployee_shouldThrowInvalidOperationException_whenDeadlineBeforeTaskCreatedDate() {
        RequestTaskAssignmentDTO dto = new RequestTaskAssignmentDTO();
        dto.setName("Assignment A");
        dto.setDeadline(LocalDate.now().minusDays(1));
        dto.setTaskId(1L);
        dto.setEmployeeId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setCreatedDateTask(LocalDate.now());

        Employee employee = new Employee();
        employee.setId(1L);

        TaskAssignment taskAssignment = new TaskAssignment();
        taskAssignment.setDeadline(dto.getDeadline());


        when(taskAssignmentMapper.toEntity(dto)).thenReturn(taskAssignment);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(InvalidOperationException.class, () -> {
            taskAssignmentService.assignTaskToEmployee(dto);
        });

        verify(taskAssignmentRepository, never()).save(any());
    }
}