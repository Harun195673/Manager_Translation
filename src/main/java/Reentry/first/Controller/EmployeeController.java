package Reentry.first.Controller;

import Reentry.first.DTO.EmployeeDTO.RequestEmployeeDTO;
import Reentry.first.DTO.EmployeeDTO.RespondEmployeeDTO;
import Reentry.first.Service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<RespondEmployeeDTO> createEmployee(@RequestBody RequestEmployeeDTO dto) {
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

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RespondEmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @RequestBody RequestEmployeeDTO dto) {

        RespondEmployeeDTO updatedEmployee = employeeService.updateEmployee(id, dto);
        return ResponseEntity.ok(updatedEmployee);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}