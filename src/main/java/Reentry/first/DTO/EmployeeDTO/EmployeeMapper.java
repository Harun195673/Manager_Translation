package Reentry.first.DTO.EmployeeDTO;

import Reentry.first.Entity.Employee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeMapper {


    public Employee toEntity (RequestEmployeeDTO dto){

        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setLanguage(dto.getLanguage());
        return employee;
    }


    public RespondEmployeeDTO toRespondDTO (Employee employee){

        RespondEmployeeDTO dto = new RespondEmployeeDTO();
        dto.setEmployeeId(employee.getId());
        dto.setName(employee.getName());
        dto.setLanguage(employee.getLanguage());
        dto.setWorkGroupId(employee.getWorkGroup().getId());
        dto.setWorkGroupName(employee.getWorkGroup().getName());

        return dto;
    }


    public List<RespondEmployeeDTO> toDTOList (List<Employee> employeeList){

         List<RespondEmployeeDTO> respondEmployeeDTOS = new ArrayList<>();

         for (Employee employee: employeeList){
             respondEmployeeDTOS.add(this.toRespondDTO(employee));
         }

        return respondEmployeeDTOS;
    }








}
