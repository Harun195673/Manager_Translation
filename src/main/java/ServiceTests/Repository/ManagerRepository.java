package management_workflow_api.Repository;
import management_workflow_api.Entity.Manager;

import management_workflow_api.Entity.WorkGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagerRepository extends JpaRepository <Manager, Long> {


    @Query("SELECT w FROM WorkGroup w JOIN FETCH w.manager")
    List<WorkGroup> getWorkGroups();

    Boolean existsByName(String name);




}
