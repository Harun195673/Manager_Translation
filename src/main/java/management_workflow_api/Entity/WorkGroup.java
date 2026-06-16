package management_workflow_api.Entity;

import management_workflow_api.DTO.WorkgroupDTO.RequestWorkGroupDTO;
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
public class WorkGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;


    @OneToMany(mappedBy = "workGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Employee> employeeList;




    public WorkGroup (RequestWorkGroupDTO dto){
        this.id = null;
        this.name = dto.getName();
    }




}
