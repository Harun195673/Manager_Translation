package ServiceTests.Service;

import ServiceTests.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import ServiceTests.DTO.TaskAssignmentDTO.RequestUpdateTaskAssignmentDTO;
import ServiceTests.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import ServiceTests.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import ServiceTests.Entity.Employee;
import ServiceTests.Entity.Task;
import ServiceTests.Entity.TaskAssignment;
import ServiceTests.Exceptions.BusinessValidationException;
import ServiceTests.Exceptions.DuplicateResourceException;
import ServiceTests.Exceptions.InvalidOperationException;
import ServiceTests.Exceptions.ResourceNotFoundException;
import ServiceTests.Repository.EmployeeRepository;
import ServiceTests.Repository.TaskAssignmentRepository;
import ServiceTests.Repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskAssignmentService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final TaskAssignmentMapper taskAssignmentMapper;

    public TaskAssignmentService(TaskRepository taskRepository,
                                 EmployeeRepository employeeRepository,
                                 TaskAssignmentRepository taskAssignmentRepository,
                                 TaskAssignmentMapper taskAssignmentMapper) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.taskAssignmentMapper = taskAssignmentMapper;
    }

    @Transactional
    public RespondTaskAssignmentDTO assignTaskToEmployee(RequestTaskAssignmentDTO dto) {
        Task task = findTaskById(dto.getTaskId());
        Employee employee = findEmployeeById(dto.getEmployeeId());

        validateDeadlineIsPresent(dto.getDeadline());
        validateDeadlineIsNotInPast(dto.getDeadline());
        validateDeadlineIsNotBeforeTaskCreatedDate(task, dto.getDeadline());

        if (taskAssignmentRepository.existsByTask_IdAndEmployee_Id(task.getId(), employee.getId())) {
            throw new DuplicateResourceException("Employee is already assigned to this task");
        }

        TaskAssignment taskAssignment = taskAssignmentMapper.toEntity(dto);
        taskAssignment.setTask(task);
        taskAssignment.setEmployee(employee);

        if (taskAssignment.getStatus() == null) {
            taskAssignment.setStatus(TaskAssignment.Status.TODO);
        }

        TaskAssignment savedTaskAssignment = taskAssignmentRepository.save(taskAssignment);

        return taskAssignmentMapper.toDTO(savedTaskAssignment);
    }

    public RespondTaskAssignmentDTO getTaskAssignmentById(Long id) {
        TaskAssignment taskAssignment = findTaskAssignmentById(id);
        return taskAssignmentMapper.toDTO(taskAssignment);
    }

    public List<RespondTaskAssignmentDTO> getAllTaskAssignments() {
        List<TaskAssignment> taskAssignments = taskAssignmentRepository.findAll();
        return taskAssignmentMapper.toDTOList(taskAssignments);
    }

    @Transactional
    public RespondTaskAssignmentDTO updateTaskAssignments(RequestUpdateTaskAssignmentDTO dto) {
        TaskAssignment oldTaskAssignment = findTaskAssignmentById(dto.getOldTaskAssignmentId());
        Task task = findTaskById(dto.getTaskId());
        Employee employee = findEmployeeById(dto.getEmployeeId());

        validateDeadlineIsPresent(dto.getDeadline());
        validateDeadlineIsNotInPast(dto.getDeadline());
        validateDeadlineIsNotBeforeTaskCreatedDate(task, dto.getDeadline());

        boolean employeeAlreadyAssignedToTask =
                taskAssignmentRepository.existsByTask_IdAndEmployee_IdAndIdNot(
                        task.getId(),
                        employee.getId(),
                        oldTaskAssignment.getId()
                );

        if (employeeAlreadyAssignedToTask) {
            throw new DuplicateResourceException("Employee is already assigned to this task");
        }

        TaskAssignment updatedTaskAssignment =
                taskAssignmentMapper.updateEntity(oldTaskAssignment, dto);

        updatedTaskAssignment.setTask(task);
        updatedTaskAssignment.setEmployee(employee);

        TaskAssignment savedTaskAssignment =
                taskAssignmentRepository.save(updatedTaskAssignment);

        return taskAssignmentMapper.toDTO(savedTaskAssignment);
    }

    @Transactional
    public void deleteTaskAssignmentById(Long id) {
        TaskAssignment taskAssignment = findTaskAssignmentById(id);
        taskAssignmentRepository.delete(taskAssignment);
    }

    @Transactional
    @Scheduled(fixedRate = 300000)
    public void markOverdueTaskAssignments() {
        List<TaskAssignment> taskAssignments = taskAssignmentRepository.findAll();

        for (TaskAssignment taskAssignment : taskAssignments) {
            if (taskAssignment.getDeadline() == null) {
                continue;
            }

            boolean deadlineIsInPast = taskAssignment.getDeadline().isBefore(LocalDate.now());
            boolean isDone = taskAssignment.getStatus() == TaskAssignment.Status.DONE;
            boolean isAlreadyOverdue = taskAssignment.getStatus() == TaskAssignment.Status.OVERDUE;

            if (deadlineIsInPast && !isDone && !isAlreadyOverdue) {
                taskAssignment.setStatus(TaskAssignment.Status.OVERDUE);
            }
        }
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    private TaskAssignment findTaskAssignmentById(Long id) {
        return taskAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskAssignment not found"));
    }

    private void validateDeadlineIsPresent(LocalDate deadline) {
        if (deadline == null) {
            throw new BusinessValidationException("Deadline is required");
        }
    }

    private void validateDeadlineIsNotInPast(LocalDate deadline) {
        if (deadline.isBefore(LocalDate.now())) {
            throw new BusinessValidationException("Deadline has to be today or in the future");
        }
    }

    private void validateDeadlineIsNotBeforeTaskCreatedDate(Task task, LocalDate deadline) {
        if (task.getCreatedDateTask().isAfter(deadline)) {
            throw new InvalidOperationException("Deadline cannot be before task creation date");
        }
    }
}