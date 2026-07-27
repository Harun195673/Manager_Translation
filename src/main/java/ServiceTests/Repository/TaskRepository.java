package ServiceTests.Repository;

import ServiceTests.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository <Task, Long> {

    Boolean existsByTitleAndMessage(String name, String message);
}
