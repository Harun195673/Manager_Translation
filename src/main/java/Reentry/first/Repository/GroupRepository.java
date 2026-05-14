package Reentry.first.Repository;

import Reentry.first.Entity.WorkGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<WorkGroup, Long> {
}
