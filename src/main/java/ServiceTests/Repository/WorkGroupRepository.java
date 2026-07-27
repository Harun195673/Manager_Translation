package ServiceTests.Repository;

import ServiceTests.Entity.WorkGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkGroupRepository extends JpaRepository<WorkGroup, Long> {


    Boolean existsByName(String name);

}
