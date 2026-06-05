package Reentry.first.Repository;

import Reentry.first.Entity.Employee;
import Reentry.first.Entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository <TaskAssignment, Long> {


    List<TaskAssignment> findByStatus(TaskAssignment.Status status);

//    private String name;
//    private LocalDate createdDate;
//    private LocalDate deadline;
//    private int hoursWorked;

    //    ///  Task2 - Find all books where year > 2000 AND title contains 'Java'.
//    @Query("SELECT b FROM Book b WHERE b.year > :year AND b.title LIKE %:keyword%")
//    List<Book> findAllBooksJava(@Param("year") int year, @Param("keyword") String keyword);

    boolean existsByNameAndAndDeadline(
            String name,
            LocalDate deadline
    );

    TaskAssignment findByTaskId(Long taskId);


    List<TaskAssignment> findByTaskIdAndEmployeeId(Long taskId, Long employeeId);

            ///  I need to find if the task is in the list
            ///  Then, I need to check if in the same line,
            ///  Is there at least one employee




}
