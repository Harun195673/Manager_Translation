package ServiceTests;

import ServiceTests.DTO.TaskDTO.RequestTaskDTO;
import ServiceTests.DTO.TaskDTO.TaskMapper;
import ServiceTests.Entity.Manager;
import ServiceTests.Entity.Task;
import ServiceTests.Entity.TaskAssignment;
import ServiceTests.Exceptions.DuplicateResourceException;
import ServiceTests.Exceptions.InvalidOperationException;
import ServiceTests.Exceptions.ResourceNotFoundException;
import ServiceTests.Repository.ManagerRepository;
import ServiceTests.Repository.TaskAssignmentRepository;
import ServiceTests.Repository.TaskRepository;
import ServiceTests.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskAssignmentRepository taskAssignmentRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_shouldThrowDuplicateResourceException_whenTaskAlreadyExists() {
        RequestTaskDTO dto = new RequestTaskDTO();
        dto.setTitle("Clean room");
        dto.setMessage("Clean the room");
        dto.setManagerId(1L);

        when(taskRepository.existsByTitleAndMessage("Clean room", "Clean the room"))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            taskService.createTask(dto);
        });

        verify(taskRepository, never()).save(any());
    }

    @Test
    void createTask_shouldThrowResourceNotFoundException_whenManagerDoesNotExist() {
        RequestTaskDTO dto = new RequestTaskDTO();
        dto.setTitle("Clean room");
        dto.setMessage("Clean the room");
        dto.setManagerId(1L);

        when(taskRepository.existsByTitleAndMessage("Clean room", "Clean the room"))
                .thenReturn(false);

        when(managerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.createTask(dto);
        });

        verify(taskRepository, never()).save(any());
    }

    @Test
    void getTaskById_shouldThrowResourceNotFoundException_whenTaskDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(1L);
        });
    }

    @Test
    void updateTask_shouldThrowInvalidOperationException_whenTaskIsAlreadyDone() {
        Long taskId = 1L;

        RequestTaskDTO dto = new RequestTaskDTO();
        dto.setTitle("New title");
        dto.setMessage("New message");
        dto.setManagerId(1L);

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Old title");
        task.setMessage("Old message");

        Manager manager = new Manager();
        manager.setId(1L);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(taskRepository.existsByTitleAndMessage("New title", "New message"))
                .thenReturn(false);

        when(managerRepository.findById(1L))
                .thenReturn(Optional.of(manager));

        when(taskAssignmentRepository.existsByTaskIdAndStatus(
                taskId,
                TaskAssignment.Status.DONE
        )).thenReturn(true);

        assertThrows(InvalidOperationException.class, () -> {
            taskService.updateTask(dto, taskId);
        });

        verify(taskRepository, never()).save(any());
    }
}