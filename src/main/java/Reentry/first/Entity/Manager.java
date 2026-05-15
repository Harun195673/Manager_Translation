package Reentry.first.Entity;

import Reentry.first.DTO.ManagerDTO.RequestManagerDTO;
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

    @OneToMany(mappedBy = "manager")
    private List<WorkGroup> workGroupList;




    public Manager (RequestManagerDTO requestManagerDTO){
            this.id = null;
            this.name = requestManagerDTO.getName();
            this.workGroupList = requestManagerDTO.getWorkGroupList();
    }


    @OneToMany(mappedBy = "manager")
    List<Task> taskList;


}
