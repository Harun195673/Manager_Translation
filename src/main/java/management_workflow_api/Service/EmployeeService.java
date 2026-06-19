package management_workflow_api.Service;

import management_workflow_api.DTO.EmployeeDTO.EmployeeMapper;
import management_workflow_api.DTO.EmployeeDTO.RequestEmployeeDTO;
import management_workflow_api.DTO.EmployeeDTO.RespondEmployeeDTO;
import management_workflow_api.DTO.EmployeeDTO.UpdateEmployeeDTO;
import management_workflow_api.DTO.WebUser.WebUserMapper;
import management_workflow_api.Entity.Employee;
import management_workflow_api.Entity.TaskAssignment;
import management_workflow_api.Entity.WebUser;
import management_workflow_api.Entity.WorkGroup;
import management_workflow_api.Exceptions.DuplicateResourceException;
import management_workflow_api.Exceptions.ResourceNotFoundException;
import management_workflow_api.Repository.EmployeeRepository;
import management_workflow_api.Repository.TaskAssignmentRepository;
import management_workflow_api.Repository.WebUserRepository;
import management_workflow_api.Repository.WorkGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final WorkGroupRepository workGroupRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final WebUserMapper webUserMapper;
    private final WebUserRepository webUserRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper employeeMapper,
                           WorkGroupRepository workGroupRepository,
                           TaskAssignmentRepository taskAssignmentRepository,
                           WebUserMapper webUserMapper,
                           WebUserRepository webUserRepository) {

        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.workGroupRepository = workGroupRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.webUserMapper = webUserMapper;
        this.webUserRepository = webUserRepository;
    }






    /// CREATE
    public RespondEmployeeDTO createEmployee (RequestEmployeeDTO dto) {

        if (employeeRepository.existsByNameAndLanguage(dto.getName(), dto.getLanguage())){
            throw new DuplicateResourceException("Employee already exists");
        }

        WorkGroup workGroup = workGroupRepository.findById(dto.getWorkGroupId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));


        Employee employee = employeeMapper.toEntity(dto);
        employee.setWorkGroup(workGroup);
        employeeRepository.save(employee);


        ///  password encyrpter
        WebUser webUser = webUserMapper.fromEmployee(dto);
        webUserRepository.save(webUser);



        return employeeMapper.toRespondDTO(employee);
    }


    /// GET BY ID
    public RespondEmployeeDTO getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        return employeeMapper.toRespondDTO(employee);
    }


    /// GET ALL
    public List<RespondEmployeeDTO> getAllEmployees() {

        List<Employee> employeeList = employeeRepository.findAll();

        return employeeMapper.toDTOList(employeeList);
    }


    /// GET ALL
    public List<RespondEmployeeDTO> getEmployeesByLanguage (Employee.Language language) {

        List<Employee> employeeList = employeeRepository.findByLanguage(language);
        return employeeMapper.toDTOList(employeeList);
    }





    /// Update
    @Transactional
    public RespondEmployeeDTO updateEmployee(UpdateEmployeeDTO dto) {

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        WorkGroup workGroup = workGroupRepository.findById(dto.getWorkGroupId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("WorkGroup not found"));

        TaskAssignment taskAssignment = taskAssignmentRepository.findById(dto.getTaskAssignmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("TaskAssignment not found"));

        if (employeeRepository.existsByNameAndLanguage(dto.getName(), dto.getLanguage())
                &&
                (!dto.getName().equals(employee.getName())
                        || !dto.getLanguage().equals(employee.getLanguage()))) {
            throw new DuplicateResourceException("Employee already exists");
        }



        employeeMapper.toUpdatedEmployee(employee, dto, workGroup);
        Employee savedEmployee = employeeRepository.save(employee);

        taskAssignment.setEmployee(savedEmployee);
        taskAssignmentRepository.save(taskAssignment);

        return employeeMapper.toRespondDTO(savedEmployee);
    }



    /// DELETE
    public void deleteEmployee (Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));


        employeeRepository.delete(employee);
    }












}
