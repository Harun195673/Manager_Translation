package Reentry.first.Service;

import Reentry.first.DTO.EmployeeDTO.EmployeeMapper;
import Reentry.first.DTO.EmployeeDTO.RequestEmployeeDTO;
import Reentry.first.DTO.EmployeeDTO.RespondEmployeeDTO;
import Reentry.first.Entity.Employee;
import Reentry.first.Entity.WorkGroup;
import Reentry.first.Repository.EmployeeRepository;
import Reentry.first.Repository.WorkGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final WorkGroupRepository workGroupRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper employeeMapper,
                           WorkGroupRepository workGroupRepository) {

        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.workGroupRepository = workGroupRepository;
    }






    /// CREATE
    public RespondEmployeeDTO createEmployee (RequestEmployeeDTO dto) {

        WorkGroup workGroup = workGroupRepository.findById(dto.getWorkGroupId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Employee employee = employeeMapper.toEntity(dto);

        employee.setWorkGroup(workGroup);

        employeeRepository.save(employee);

        return employeeMapper.toRespondDTO(employee);
    }


    /// GET BY ID
    public RespondEmployeeDTO getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        return employeeMapper.toRespondDTO(employee);
    }


    /// GET ALL
    public List<RespondEmployeeDTO> getAllEmployees() {

        List<Employee> employeeList = employeeRepository.findAll();

        return employeeMapper.toDTOList(employeeList);
    }





    /// UPDATE
    public RespondEmployeeDTO updateEmployee (Long id,
                                            RequestEmployeeDTO dto) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        employee.setName(dto.getName());

        employeeRepository.save(employee);

        return employeeMapper.toRespondDTO(employee);
    }


    /// DELETE
    public void deleteEmployee (Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        employeeRepository.delete(employee);
    }









}
