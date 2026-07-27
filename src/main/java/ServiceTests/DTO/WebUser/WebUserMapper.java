package ServiceTests.DTO.WebUser;

import ServiceTests.DTO.EmployeeDTO.RequestEmployeeDTO;
import ServiceTests.DTO.ManagerDTO.RequestManagerDTO;
import ServiceTests.Entity.WebUser;
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