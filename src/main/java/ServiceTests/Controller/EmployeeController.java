package ServiceTests.Controller;

import ServiceTests.DTO.EmployeeDTO.RequestEmployeeDTO;
import ServiceTests.DTO.EmployeeDTO.RespondEmployeeDTO;
import ServiceTests.DTO.EmployeeDTO.UpdateEmployeeDTO;
import ServiceTests.Entity.Employee;
import ServiceTests.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<RespondEmployeeDTO> createEmployee(@RequestBody @Valid RequestEmployeeDTO dto) {
        RespondEmployeeDTO response = employeeService.createEmployee(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RespondEmployeeDTO> getEmployeeById(@PathVariable Long id) {
        RespondEmployeeDTO response = employeeService.getEmployeeById(id); // consider renaming service method
        return ResponseEntity.ok(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<RespondEmployeeDTO>> getAllEmployees() {
        List<RespondEmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // GET ALL
    @GetMapping("/language/{language}")
    public List<RespondEmployeeDTO> filterEmployeesByLanguage(
            @PathVariable Employee.Language language) {

        return employeeService.getEmployeesByLanguage(language);
    }





    // UPDATE
    @PutMapping("/update")
    public ResponseEntity<RespondEmployeeDTO> updateEmployee(
            @RequestBody @Valid UpdateEmployeeDTO dto) {

        RespondEmployeeDTO updatedEmployee = employeeService.updateEmployee(dto);
        return ResponseEntity.ok(updatedEmployee);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}