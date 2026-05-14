package Reentry.first.Entity;

import Reentry.first.DTO.WorkgroupDTO.RequestWorkGroupDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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


    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public WorkGroup (RequestWorkGroupDTO dto){
        this.id = null;
        this.name = dto.getName();
        this.manager = dto.getManager();
    }




}
