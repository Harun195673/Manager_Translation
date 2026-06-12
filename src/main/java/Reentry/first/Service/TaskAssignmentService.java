package Reentry.first.Service;

import Reentry.first.DTO.EmployeeDTO.EmployeeMapper;
import Reentry.first.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import Reentry.first.DTO.TaskAssignmentDTO.RequestUpdateTaskAssignmentDTO;
import Reentry.first.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import Reentry.first.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import Reentry.first.DTO.TaskDTO.TaskMapper;
import Reentry.first.Entity.Employee;
import Reentry.first.Entity.Task;
import Reentry.first.Entity.TaskAssignment;
import Reentry.first.Exceptions.BusinessValidationException;
import Reentry.first.Exceptions.DuplicateResourceException;
import Reentry.first.Exceptions.InvalidOperationException;
import Reentry.first.Exceptions.ResourceNotFoundException;
import Reentry.first.Repository.EmployeeRepository;
import Reentry.first.Repository.TaskAssignmentRepository;
import Reentry.first.Repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class TaskAssignmentService {


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final TaskAssignmentMapper taskAssignmentMapper;

    public TaskAssignmentService(TaskRepository taskRepository,
                                 TaskMapper taskMapper,
                                 EmployeeRepository employeeRepository,
                                 EmployeeMapper employeeMapper,
                                 TaskAssignmentRepository taskAssignmentRepository,
                                 TaskAssignmentMapper taskAssignmentMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.taskAssignmentMapper = taskAssignmentMapper;
    }



    ///  Create
    public RespondTaskAssignmentDTO assignTaskToEmployee (RequestTaskAssignmentDTO dto){

//        if(taskAssignmentRepository.existsByNameAndAndDeadline(
//                dto.getName(),
//                dto.getDeadline())) {
//               throw new BusinessValidationException("TaskAssignment already exists");
//        }


        TaskAssignment taskAssignment = taskAssignmentMapper.toEntity(dto);

        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        if (task.getCreatedDateTask().isAfter(taskAssignment.getDeadline())){
            throw new InvalidOperationException("Deadline cannot be before start date");
        }



        /// Check if task is completed
        TaskAssignment taskAssignmentFoundByTaskId = taskAssignmentRepository.findByTaskId(task.getId());
        if (taskAssignmentFoundByTaskId != null &&
                taskAssignmentFoundByTaskId.getStatus() == TaskAssignment.Status.DONE) {
            throw new InvalidOperationException("Cannot assign employee to a completed task");
        }

        taskAssignment.setTask(task);
        taskAssignment.setEmployee(employee);
        taskAssignmentRepository.save(taskAssignment);

        return taskAssignmentMapper.toDTO(taskAssignment);
    }















    ///  Get by id
    public RespondTaskAssignmentDTO getTaskAssignmentById (Long id){

        TaskAssignment taskAssignment = taskAssignmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        return  taskAssignmentMapper.toDTO(taskAssignment);
    }


    ///  Get all
    public List<RespondTaskAssignmentDTO> getAllTaskAssignments (){

        List<TaskAssignment> taskAssignmentList = taskAssignmentRepository.findAll();
        return  taskAssignmentMapper.toDTOList(taskAssignmentList);
    }





    ///  Update TaskAssignment
    public RespondTaskAssignmentDTO updateTaskAssignments (RequestUpdateTaskAssignmentDTO dto){


        if (dto.getDeadline().isBefore(LocalDate.now())){
            throw new BusinessValidationException ("Deadline has to be in the future");
        }


        TaskAssignment oldTaskAssignment = taskAssignmentRepository.findById(dto.getOldTaskAssignmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("TaskAssignment not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));


        if (taskAssignmentRepository.existsByNameAndAndDeadline(
                dto.getName(),
                dto.getDeadline())
                &&
                (!oldTaskAssignment.getName().equals(dto.getName())
                        ||
                        !oldTaskAssignment.getDeadline().equals(dto.getDeadline()))
        ) {

            throw new DuplicateResourceException("TaskAssignment already exists");
        }


        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));



        if (dto.getStatus().equals("DONE") && dto.getDeadline() != null) {
            
            if (task.getCreatedDateTask().isAfter(dto.getDeadline())
                    || LocalDate.now().isAfter(dto.getDeadline())) {

                throw new InvalidOperationException("Deadline is before created time");
            }
        }





        TaskAssignment updatedTaskAssignment = taskAssignmentMapper.updateEntity(oldTaskAssignment, dto);
        updatedTaskAssignment.setTask(task);
        updatedTaskAssignment.setEmployee(employee);
        taskAssignmentRepository.save(updatedTaskAssignment);

        return taskAssignmentMapper.toDTO(updatedTaskAssignment);
    }





    ///  Delete by id
    public void deleteTaskAssignmentById (Long id){

        TaskAssignment taskAssignment = taskAssignmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));
        taskAssignmentRepository.deleteById(id);
    }





    @Transactional
    @Scheduled(fixedRate = 300000)
    public void markOverdueTaskAssignments() {
        List<TaskAssignment> taskAssignmentList = taskAssignmentRepository.findAll();
        for (TaskAssignment taskAssignment : taskAssignmentList) {
            if (taskAssignment.getDeadline().isBefore(LocalDate.now()) && taskAssignment.getStatus() != TaskAssignment.Status.OVERDUE) {
                taskAssignment.setStatus(TaskAssignment.Status.OVERDUE);
                taskAssignmentRepository.save(taskAssignment);
            }
        }
    }
























}
