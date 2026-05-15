package Reentry.first.Service;

import Reentry.first.DTO.TaskDTO.RequestTaskDTO;
import Reentry.first.DTO.TaskDTO.RespondTaskDTO;
import Reentry.first.DTO.TaskDTO.TaskMapper;
import Reentry.first.Entity.Manager;
import Reentry.first.Entity.Task;
import Reentry.first.Repository.TaskRepository;
import Reentry.first.Repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ManagerRepository managerRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository,
                       ManagerRepository managerRepository,
                       TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.managerRepository = managerRepository;
        this.taskMapper = taskMapper;
    }


    public RespondTaskDTO createTask(RequestTaskDTO dto){
        Manager manager = managerRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        Task task = taskMapper.toEntity(dto, manager);
        taskRepository.save(task);

        return taskMapper.toRespondDTO(task);
    }



    public List<RespondTaskDTO> getAllTasks(){
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toRespondDTO)
                .collect(Collectors.toList());
    }

    public RespondTaskDTO getTaskById(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.toRespondDTO(task);
    }



    public void deleteTask(Long id){
        if(!taskRepository.existsById(id)){
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }



}