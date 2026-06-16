package management_workflow_api.Entity;

import management_workflow_api.DTO.ManagerDTO.RequestManagerDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Manager {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkGroup> workGroupList;


    public Manager (RequestManagerDTO requestManagerDTO){
            this.id = null;
            this.name = requestManagerDTO.getName();
    }


    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Task> taskList;


}
