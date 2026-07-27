package ServiceTests.Entity;


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


        @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "webUser_Id")
    WebUser webUser;




    @Enumerated(EnumType.STRING)
    private Language language;

    public enum Language {
        TURKISH,
        POLISH,
        RUSSIAN,
        ARABIC,
        SPANISH,
        FRENCH,
        GERMAN,
        ENGLISH
    }

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<TaskAssignment> taskAssignmentList;






}
