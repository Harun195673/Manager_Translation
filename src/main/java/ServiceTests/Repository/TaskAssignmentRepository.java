package management_workflow_api.Repository;

import management_workflow_api.Entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository <TaskAssignment, Long> {


    List<TaskAssignment> findByStatus(TaskAssignment.Status status);

    boolean existsByNameAndAndDeadline(
            String name,
            LocalDate deadline
    );

    TaskAssignment findByTaskId(Long taskId);


    List<TaskAssignment> findByTaskIdAndEmployeeId(Long taskId, Long employeeId);

    boolean existsByTask_IdAndEmployee_Id(Long taskId, Long employeeId);

    boolean existsByTask_IdAndEmployee_IdAndIdNot(Long taskId, Long employeeId, Long id);

    boolean existsByTaskIdAndStatus(Long taskId, TaskAssignment.Status status);




}
