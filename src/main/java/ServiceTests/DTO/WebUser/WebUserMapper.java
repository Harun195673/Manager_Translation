package management_workflow_api.DTO.WebUser;

import management_workflow_api.DTO.EmployeeDTO.RequestEmployeeDTO;
import management_workflow_api.DTO.ManagerDTO.RequestManagerDTO;
import management_workflow_api.Entity.Employee;
import management_workflow_api.Entity.Manager;
import management_workflow_api.Entity.WebUser;
import org.springframework.stereotype.Component;

@Component
public class WebUserMapper {

    public WebUserResponseDTO toResponseDto(WebUser webUser) {
        return new WebUserResponseDTO(
                webUser.getId(),
                webUser.getUsername(),
                webUser.getRole()
        );
    }

    public WebUser fromEmployee(RequestEmployeeDTO dto){

        WebUser webUser = new WebUser();
        webUser.setRole("EMPLOYEE");
        webUser.setUsername(dto.getUserName());
        webUser.setPassword(dto.getPassword());

        return webUser;
    }

    public WebUser fromManager (RequestManagerDTO dto){

        WebUser webUser = new WebUser();
        webUser.setRole("MANAGER");
        webUser.setUsername(dto.getUserName());
        webUser.setPassword(dto.getPassword());

        return webUser;
    }




}