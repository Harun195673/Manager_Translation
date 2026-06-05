package Reentry.first.Repository;

import Reentry.first.Entity.Employee;
import Reentry.first.Entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByLanguage(Employee.Language language);

    Boolean existsByNameAndLanguage(String name, Employee.Language language);
}
