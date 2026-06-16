package management_workflow_api.Repository;

import management_workflow_api.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByLanguage(Employee.Language language);

    Boolean existsByNameAndLanguage(String name, Employee.Language language);
}
