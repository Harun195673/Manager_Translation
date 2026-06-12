package Reentry.first.Service;

import Reentry.first.DTO.EmployeeDTO.EmployeeMapper;
import Reentry.first.DTO.ManagerDTO.ManagerMapper;
import Reentry.first.DTO.ManagerDTO.RequestManagerDTO;
import Reentry.first.DTO.ManagerDTO.RequestWorkFlowDTO;
import Reentry.first.DTO.ManagerDTO.RespondManagerDTO;
import Reentry.first.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import Reentry.first.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import Reentry.first.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import Reentry.first.DTO.TaskDTO.TaskMapper;
import Reentry.first.DTO.WorkgroupDTO.WorkGroupMapper;
import Reentry.first.Entity.*;
import Reentry.first.Exceptions.DuplicateResourceException;
import Reentry.first.Exceptions.InvalidOperationException;
import Reentry.first.Exceptions.ResourceNotFoundException;
import Reentry.first.Repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final WorkGroupRepository workGroupRepository;
    private final WorkGroupMapper workGroupMapper;
    private final ManagerMapper managerMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final TaskAssignmentService taskAssignmentService;
    private final TaskAssignmentMapper taskAssignmentMapper;
    private final TranslationService translationService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public ManagerService(ManagerRepository managerRepository,
                          WorkGroupRepository workGroupRepository,
                          WorkGroupMapper workGroupMapper,
                          ManagerMapper managerMapper,
                          EmployeeRepository employeeRepository,
                          EmployeeMapper employeeMapper,
                          TaskAssignmentRepository taskAssignmentRepository,
                          TaskAssignmentService taskAssignmentService,
                          TaskAssignmentMapper taskAssignmentMapper,
                          TranslationService translationService,
                          TaskRepository taskRepository,
                          TaskMapper taskMapper) {
        this.managerRepository = managerRepository;
        this.workGroupRepository = workGroupRepository;
        this.workGroupMapper = workGroupMapper;
        this.managerMapper = managerMapper;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.taskAssignmentService = taskAssignmentService;
        this.taskAssignmentMapper = taskAssignmentMapper;
        this.translationService = translationService;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }



    /// CREATE
    public RespondManagerDTO createManager(RequestManagerDTO dto) {

        if (managerRepository.existsByName(dto.getName())){
            throw new DuplicateResourceException("Manager already exists. " +
                                                 "Choose a different name");
        }

        Manager manager = managerMapper.toEntity(dto);
        managerRepository.save(manager);
        return managerMapper.toRespondDTO(manager);
    }






    /// GET BY ID
    public RespondManagerDTO getManagerById(Long id) {

        Manager manager = managerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manager not found"));

        return managerMapper.toRespondDTO(manager);
    }


    /// GET ALL
    public List<RespondManagerDTO> getAllManagers() {

        List<Manager> managers = managerRepository.findAll();

        return managerMapper.respondManagerDTOList(managers);
    }





    /// UPDATE
    public RespondManagerDTO updateManager(Long id,
                                           RequestManagerDTO dto) {


        Manager manager = managerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manager not found"));


        if (!managerRepository.existsByName(dto.getName())
                        && !manager.getName().equals(manager.getName())){
            throw new DuplicateResourceException("Manager already exists. Choose a different name");
        }

        manager.setName(dto.getName());

        managerRepository.save(manager);

        return managerMapper.toRespondDTO(manager);
    }


    /// DELETE
    public void deleteManager(Long id) {

        Manager manager = managerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manager not found"));


        if (manager.getWorkGroupList().size() > 0){
            throw new InvalidOperationException("Cannot delete Manager with workGroup");
        }

        managerRepository.delete(manager);
    }






    public List<RespondTaskAssignmentDTO> taskTranslationWorkflow (RequestWorkFlowDTO workFlowDto) {

        ///  Get Manager
        Manager manager = managerRepository.findById(workFlowDto.getManagerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manager not found"));

        ///  Get WorkGroup
        WorkGroup workGroup = workGroupRepository.findById(workFlowDto.getWorkGroupId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("WorkGroup not found"));


        ///  Check Manager -- Workgroup relation
        if (!manager.getId().equals(workGroup.getManager().getId())){
            throw new ResourceNotFoundException("manager_id and workGroupId are not equal");
        }

        ///  Get Task
        Task task = taskRepository.findById(workFlowDto.getTaskId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));


        HashMap<Employee.Language, List<Employee>> employeeMap =
                workGroupMapper.getEmployeeMap(workGroup.getEmployeeList());
        List<RespondTaskAssignmentDTO> respondTaskAssignmentDTOList = new ArrayList<>();



        for (Map.Entry<Employee.Language, List<Employee>> entry : employeeMap.entrySet()) {


            ///  new values
            String newLanguage = entry.getKey().name();
            String taskMessage = task.getMessage();
            String translatedMessage = translationService.translateText(taskMessage,"German", newLanguage);


            ///  create and save the translated task
            Task translatedTask =
                    taskMapper.createTranslatedTask(task, translatedMessage);
            taskRepository.save(translatedTask);


            List<Employee> employeeList = entry.getValue();



             for (Employee employee: employeeList){

                 RequestTaskAssignmentDTO requestTaskAssignmentDTO =
                         taskAssignmentMapper.buildTaskAssignmentRequest(
                                 workFlowDto,
                                 employee,
                                 translatedTask,
                                 newLanguage
                         );

                 RespondTaskAssignmentDTO respondDTO = taskAssignmentService.assignTaskToEmployee(requestTaskAssignmentDTO);
                 respondTaskAssignmentDTOList.add(respondDTO);
             }

        }
        return respondTaskAssignmentDTOList;
    }



















}