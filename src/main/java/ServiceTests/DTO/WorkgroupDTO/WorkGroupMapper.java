package ServiceTests.DTO.WorkgroupDTO;

import ServiceTests.Entity.Employee;
import ServiceTests.Entity.WorkGroup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class WorkGroupMapper {


    public WorkGroup toEntity (RequestWorkGroupDTO dto){
        WorkGroup workGroup = new WorkGroup(dto);
        return workGroup;
    }

    public RespondWorkGroupDTO toRespondDTO (WorkGroup workGroup){
        RespondWorkGroupDTO respondDTO = new RespondWorkGroupDTO(workGroup);
        return respondDTO;
    }

    public List<RespondWorkGroupDTO> requestWorkgroupRespondDTOList (List<WorkGroup> workGroupList){

        List<RespondWorkGroupDTO> respondWorkGroupDTOList = new ArrayList<>();
        for (WorkGroup workGroup: workGroupList){
            respondWorkGroupDTOList.add(this.toRespondDTO(workGroup));
        }
        return respondWorkGroupDTOList;
    }


    public HashMap<Employee.Language, List<Employee>> getEmployeeMap(List<Employee> employeeList) {
        HashMap<Employee.Language, List<Employee>> employeeMap = new HashMap<>();

        for (Employee employee : employeeList) {
            Employee.Language lang = employee.getLanguage();

            // If the map doesn’t have a list yet, create it
            employeeMap.computeIfAbsent(lang, k -> new ArrayList<>());

            // Add the employee to the list for this language
            employeeMap.get(lang).add(employee);
        }

        return employeeMap;
    }




}
