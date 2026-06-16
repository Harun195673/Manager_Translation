package management_workflow_api.Repository;

import management_workflow_api.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository <Task, Long> {

    Boolean existsByTitleAndMessage(String name, String message);
}
