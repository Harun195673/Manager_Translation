package management_workflow_api.Service;

import management_workflow_api.DTO.TaskDTO.RequestTaskDTO;
import management_workflow_api.DTO.TaskDTO.RespondTaskDTO;
import management_workflow_api.DTO.TaskDTO.TaskMapper;
import management_workflow_api.Entity.Manager;
import management_workflow_api.Entity.Task;
import management_workflow_api.Entity.TaskAssignment;
import management_workflow_api.Exceptions.DuplicateResourceException;
import management_workflow_api.Exceptions.InvalidOperationException;
import management_workflow_api.Exceptions.ResourceNotFoundException;
import management_workflow_api.Repository.ManagerRepository;
import management_workflow_api.Repository.TaskAssignmentRepository;
import management_workflow_api.Repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ManagerRepository managerRepository;
    private final TaskMapper taskMapper;
    private final TaskAssignmentRepository taskAssignmentRepository;

    public TaskService(TaskRepository taskRepository,
                       ManagerRepository managerRepository,
                       TaskMapper taskMapper,
                       TaskAssignmentRepository taskAssignmentRepository) {
        this.taskRepository = taskRepository;
        this.managerRepository = managerRepository;
        this.taskMapper = taskMapper;
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    public RespondTaskDTO createTask(RequestTaskDTO dto) {

        if (taskRepository.existsByTitleAndMessage(dto.getTitle(), dto.getMessage())) {
            throw new DuplicateResourceException("Task already exists. Create a different Task");
        }

        Manager manager = managerRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        Task task = taskMapper.toEntity(dto, manager);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toRespondDTO(savedTask);
    }

    public List<RespondTaskDTO> getAllTasks() {

        List<Task> taskList = taskRepository.findAll();

        return taskMapper.respondTaskDTOList(taskList);
    }

    public RespondTaskDTO getTaskById(Long id) {

        Task task = getTaskOrThrow(id);

        return taskMapper.toRespondDTO(task);
    }

    public void deleteTask(Long id) {

        Task task = getTaskOrThrow(id);

        taskRepository.delete(task);
    }

    @Transactional
    public RespondTaskDTO updateTask(RequestTaskDTO dto, Long taskId) {

        Task task = getTaskOrThrow(taskId);

        boolean duplicateExists = taskRepository.existsByTitleAndMessage(
                dto.getTitle(),
                dto.getMessage()
        );

        boolean taskValuesChanged =
                !task.getTitle().equals(dto.getTitle())
                        || !task.getMessage().equals(dto.getMessage());

        if (duplicateExists && taskValuesChanged) {
            throw new DuplicateResourceException("Task already exists. Use different values");
        }

        Manager manager = managerRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        boolean hasCompletedAssignment =
                taskAssignmentRepository.existsByTaskIdAndStatus(
                        task.getId(),
                        TaskAssignment.Status.DONE
                );

        if (hasCompletedAssignment) {
            throw new InvalidOperationException("Cannot edit completed Task");
        }

        task.setTitle(dto.getTitle());
        task.setMessage(dto.getMessage());
        task.setManager(manager);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toRespondDTO(savedTask);
    }

    private Task getTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }
}