package management_workflow_api.Service;

import management_workflow_api.DTO.EmployeeDTO.EmployeeMapper;
import management_workflow_api.DTO.ManagerDTO.ManagerMapper;
import management_workflow_api.DTO.ManagerDTO.RequestManagerDTO;
import management_workflow_api.DTO.ManagerDTO.RequestWorkFlowDTO;
import management_workflow_api.DTO.ManagerDTO.RespondManagerDTO;
import management_workflow_api.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import management_workflow_api.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import management_workflow_api.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import management_workflow_api.DTO.TaskDTO.TaskMapper;
import management_workflow_api.DTO.WebUser.WebUserMapper;
import management_workflow_api.DTO.WorkgroupDTO.WorkGroupMapper;
import management_workflow_api.Entity.*;
import management_workflow_api.Exceptions.DuplicateResourceException;
import management_workflow_api.Exceptions.InvalidOperationException;
import management_workflow_api.Exceptions.ResourceNotFoundException;
import management_workflow_api.Repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final WebUserRepository webUserRepository;
    private final WebUserMapper webUserMapper;
    private final PasswordEncoder passwordEncoder;

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
                          TaskMapper taskMapper,
                          WebUserRepository webUserRepository,
                          WebUserMapper webUserMapper,
                          PasswordEncoder passwordEncoder) {
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
        this.webUserRepository = webUserRepository;
        this.webUserMapper = webUserMapper;
        this.passwordEncoder = passwordEncoder;
    }



    /// CREATE
    public RespondManagerDTO createManager(RequestManagerDTO dto) {

        if (managerRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException(
                    "Manager already exists. Choose a different name"
            );
        }

        Manager manager = managerMapper.toEntity(dto);

        WebUser webUser = webUserMapper.fromManager(dto);

        webUser.setPassword(
                passwordEncoder.encode(webUser.getPassword())
        );

        manager.setWebUser(webUser);
        webUser.setManager(manager);

        Manager savedManager = managerRepository.save(manager);

        return managerMapper.toRespondDTO(savedManager);
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