package Reentry.first.Repository;

import Reentry.first.Entity.WorkGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkGroupRepository extends JpaRepository<WorkGroup, Long> {


    Boolean existsByName(String name);

}
