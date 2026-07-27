package ServiceTests.Service;

import ServiceTests.DTO.ManagerDTO.RequestWorkFlowDTO;
import ServiceTests.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import ServiceTests.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import ServiceTests.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import ServiceTests.DTO.TaskDTO.TaskMapper;
import ServiceTests.DTO.WorkgroupDTO.WorkGroupMapper;
import ServiceTests.Entity.Employee;
import ServiceTests.Entity.Manager;
import ServiceTests.Entity.Task;
import ServiceTests.Entity.WorkGroup;
import ServiceTests.Exceptions.InvalidOperationException;
import ServiceTests.Exceptions.ResourceNotFoundException;
import ServiceTests.Repository.ManagerRepository;
import ServiceTests.Repository.TaskRepository;
import ServiceTests.Repository.WorkGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerWorkflowService {

    private final ManagerRepository managerRepository;
    private final WorkGroupRepository workGroupRepository;
    private final TaskRepository taskRepository;
    private final WorkGroupMapper workGroupMapper;
    private final TaskMapper taskMapper;
    private final TaskAssignmentMapper taskAssignmentMapper;
    private final TaskAssignmentService taskAssignmentService;
    private final TranslationService translationService;

    public ManagerWorkflowService(ManagerRepository managerRepository,
                                  WorkGroupRepository workGroupRepository,
                                  TaskRepository taskRepository,
                                  WorkGroupMapper workGroupMapper,
                                  TaskMapper taskMapper,
                                  TaskAssignmentMapper taskAssignmentMapper,
                                  TaskAssignmentService taskAssignmentService,
                                  TranslationService translationService) {
        this.managerRepository = managerRepository;
        this.workGroupRepository = workGroupRepository;
        this.taskRepository = taskRepository;
        this.workGroupMapper = workGroupMapper;
        this.taskMapper = taskMapper;
        this.taskAssignmentMapper = taskAssignmentMapper;
        this.taskAssignmentService = taskAssignmentService;
        this.translationService = translationService;
    }

    @Transactional
    public List<RespondTaskAssignmentDTO> taskTranslationWorkflow(RequestWorkFlowDTO workFlowDto) {

        Manager manager = findManagerById(workFlowDto.getManagerId());
        WorkGroup workGroup = findWorkGroupById(workFlowDto.getWorkGroupId());
        Task task = findTaskById(workFlowDto.getTaskId());

        validateManagerOwnsWorkGroup(manager, workGroup);

        HashMap<Employee.Language, List<Employee>> employeesByLanguage =
                workGroupMapper.getEmployeeMap(workGroup.getEmployeeList());

        List<RespondTaskAssignmentDTO> responseList = new ArrayList<>();

        for (Map.Entry<Employee.Language, List<Employee>> entry : employeesByLanguage.entrySet()) {

            Employee.Language targetLanguage = entry.getKey();
            List<Employee> employees = entry.getValue();

            Task translatedTask = createTranslatedTask(task, targetLanguage);

            for (Employee employee : employees) {

                RequestTaskAssignmentDTO requestDto =
                        taskAssignmentMapper.buildTaskAssignmentRequest(
                                workFlowDto,
                                employee,
                                translatedTask,
                                targetLanguage
                        );

                RespondTaskAssignmentDTO responseDto =
                        taskAssignmentService.assignTaskToEmployee(requestDto);

                responseList.add(responseDto);
            }
        }

        return responseList;
    }




    private Manager findManagerById(Long id) {

        return managerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manager not found"));
    }

    private WorkGroup findWorkGroupById(Long id) {

        return workGroupRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("WorkGroup not found"));
    }

    private Task findTaskById(Long id) {

        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));
    }

    private void validateManagerOwnsWorkGroup(Manager manager, WorkGroup workGroup) {

        if (!manager.getId().equals(workGroup.getManager().getId())) {
            throw new InvalidOperationException(
                    "This workGroup does not belong to the given manager"
            );
        }
    }

    private Task createTranslatedTask(Task originalTask, Employee.Language targetLanguage) {

        String translatedMessage = translationService.translateText(
                originalTask.getMessage(),
                Employee.Language.GERMAN,
                targetLanguage
        );

        Task translatedTask =
                taskMapper.createTranslatedTask(originalTask, translatedMessage);

        return taskRepository.save(translatedTask);
    }
}