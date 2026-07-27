package management_workflow_api.Repository;

import management_workflow_api.Entity.WorkGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkGroupRepository extends JpaRepository<WorkGroup, Long> {


    Boolean existsByName(String name);

}
