package Reentry.first.Service;

import Reentry.first.DTO.EmployeeDTO.EmployeeMapper;
import Reentry.first.DTO.EmployeeDTO.RequestEmployeeDTO;
import Reentry.first.DTO.EmployeeDTO.RespondEmployeeDTO;
import Reentry.first.DTO.EmployeeDTO.UpdateEmployeeDTO;
import Reentry.first.Entity.Employee;
import Reentry.first.Entity.TaskAssignment;
import Reentry.first.Entity.WorkGroup;
import Reentry.first.Exceptions.DuplicateResourceException;
import Reentry.first.Exceptions.ResourceNotFoundException;
import Reentry.first.Repository.EmployeeRepository;
import Reentry.first.Repository.TaskAssignmentRepository;
import Reentry.first.Repository.WorkGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final WorkGroupRepository workGroupRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper employeeMapper,
                           WorkGroupRepository workGroupRepository,
                           TaskAssignmentRepository taskAssignmentRepository) {

        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.workGroupRepository = workGroupRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
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
