package Reentry.first.Entity;


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
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workGroup_id")
    private WorkGroup workGroup;


    @Enumerated(EnumType.STRING)
    private Language language;

    public enum Language {
        Turkish,
        Polish,
        Russian,
        Arabic,
        Spanish,
        French
    }


    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<TaskAssignment> taskAssignmentList;






}
