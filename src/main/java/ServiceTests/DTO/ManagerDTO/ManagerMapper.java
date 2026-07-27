package ServiceTests.DTO.ManagerDTO;

import ServiceTests.Entity.Manager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ManagerMapper {


    public Manager toEntity (RequestManagerDTO dto){
        Manager manager = new Manager(dto);
        return manager;
    }

    public RespondManagerDTO toRespondDTO (Manager manager){
            RespondManagerDTO dto = new RespondManagerDTO(manager);
            return dto;
    }

    public List<RespondManagerDTO> respondManagerDTOList (List<Manager> managerList){

        List<RespondManagerDTO> respondManagerDTOList = new ArrayList<>();
        for (Manager manager: managerList){
            respondManagerDTOList.add(this.toRespondDTO(manager));
        }

        return respondManagerDTOList;
    }









}
