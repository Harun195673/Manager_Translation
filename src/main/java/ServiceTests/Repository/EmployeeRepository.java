package ServiceTests.Repository;

import ServiceTests.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByLanguage(Employee.Language language);

    Boolean existsByNameAndLanguageAndIdNot (String name, Employee.Language language, Long id);

    Boolean existsByNameAndLanguage (String name, Employee.Language language);

}
