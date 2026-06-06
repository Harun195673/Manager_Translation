package Reentry.first;


import Reentry.first.DTO.TaskDTO.RequestTaskDTO;
import Reentry.first.DTO.TaskDTO.TaskMapper;
import Reentry.first.Entity.Manager;
import Reentry.first.Entity.Task;
import Reentry.first.Entity.TaskAssignment;
import Reentry.first.Exceptions.DuplicateResourceException;
import Reentry.first.Exceptions.InvalidOperationException;
import Reentry.first.Exceptions.ResourceNotFoundException;
import Reentry.first.Repository.ManagerRepository;
import Reentry.first.Repository.TaskAssignmentRepository;
import Reentry.first.Repository.TaskRepository;
import Reentry.first.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        dto.setTaskAssignmentId(1L);

        Task task = new Task();
        task.setId(1L);

        TaskAssignment taskAssignment = new TaskAssignment();
        taskAssignment.setStatus(TaskAssignment.Status.DONE);
        taskAssignment.setId(1L);

        Manager manager = new Manager();
        manager.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskAssignmentRepository.findById(1L)).thenReturn(Optional.of(taskAssignment));
        when(taskAssignmentRepository.findByTaskId(1L))
                .thenReturn(taskAssignment);
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        assertThrows(InvalidOperationException.class, () -> {
            taskService.updateTask(dto, taskId);
        });

        verify(taskRepository, never()).save(any());
    }
}