package management_workflow_api.Service;

import jakarta.transaction.Transactional;
import management_workflow_api.DTO.EmployeeDTO.EmployeeMapper;
import management_workflow_api.DTO.EmployeeDTO.RequestEmployeeDTO;
import management_workflow_api.DTO.EmployeeDTO.RespondEmployeeDTO;
import management_workflow_api.DTO.EmployeeDTO.UpdateEmployeeDTO;
import management_workflow_api.DTO.WebUser.WebUserMapper;
import management_workflow_api.Entity.Employee;
import management_workflow_api.Entity.WebUser;
import management_workflow_api.Entity.WorkGroup;
import management_workflow_api.Exceptions.DuplicateResourceException;
import management_workflow_api.Exceptions.ResourceNotFoundException;
import management_workflow_api.Repository.EmployeeRepository;
import management_workflow_api.Repository.WorkGroupRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final WorkGroupRepository workGroupRepository;
    private final WebUserMapper webUserMapper;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper employeeMapper,
                           WorkGroupRepository workGroupRepository,
                           WebUserMapper webUserMapper,
                           PasswordEncoder passwordEncoder) {

        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.workGroupRepository = workGroupRepository;
        this.webUserMapper = webUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /// CREATE
    @Transactional
    public RespondEmployeeDTO createEmployee(RequestEmployeeDTO dto) {

        if (employeeRepository.existsByNameAndLanguage(dto.getName(), dto.getLanguage())) {
            throw new DuplicateResourceException("Employee already exists");
        }

        WorkGroup workGroup = this.findWorkGroup(dto.getWorkGroupId());

        Employee employee = employeeMapper.toEntity(dto);
        employee.setWorkGroup(workGroup);

        WebUser webUser = webUserMapper.fromEmployee(dto);
        webUser.setPassword(passwordEncoder.encode(webUser.getPassword()));

        employee.setWebUser(webUser);
        webUser.setEmployee(employee);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toRespondDTO(savedEmployee);
    }

    /// GET BY ID
    public RespondEmployeeDTO getEmployeeById(Long id) {

        Employee employee = this.findEmployee(id);

        return employeeMapper.toRespondDTO(employee);
    }

    /// GET ALL
    public List<RespondEmployeeDTO> getAllEmployees() {

        List<Employee> employeeList = employeeRepository.findAll();

        return employeeMapper.toDTOList(employeeList);
    }

    /// GET BY LANGUAGE
    public List<RespondEmployeeDTO> getEmployeesByLanguage(Employee.Language language) {

        List<Employee> employeeList = employeeRepository.findByLanguage(language);

        return employeeMapper.toDTOList(employeeList);
    }

    /// UPDATE
    @Transactional
    public RespondEmployeeDTO updateEmployee(UpdateEmployeeDTO dto) {

        Employee employee = this.findEmployee(dto.getEmployeeId());
        WorkGroup workGroup = this.findWorkGroup(dto.getWorkGroupId());

        if (employeeRepository.existsByNameAndLanguageAndIdNot(
                dto.getName(), dto.getLanguage(), dto.getEmployeeId())) {
            throw new DuplicateResourceException("Employee already exists");
        }

        employeeMapper.toUpdatedEmployee(employee, dto, workGroup);

        return employeeMapper.toRespondDTO(employee);
    }

    /// DELETE
    @Transactional
    public void deleteEmployee(Long id) {

        Employee employee = this.findEmployee(id);

        employeeRepository.delete(employee);
    }



    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    private WorkGroup findWorkGroup(Long id) {
        return workGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkGroup not found"));
    }
}