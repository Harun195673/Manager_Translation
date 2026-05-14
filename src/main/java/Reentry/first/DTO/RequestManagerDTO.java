package Reentry.first.DTO;

import Reentry.first.Entity.WorkGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestManagerDTO {

    private String name;
    private List<WorkGroup> workGroupList;
}
