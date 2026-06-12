package Reentry.first;

import Reentry.first.Entity.Employee;
import Reentry.first.Entity.Manager;
import Reentry.first.Entity.Task;
import Reentry.first.Entity.TaskAssignment;
import Reentry.first.Entity.WorkGroup;
import Reentry.first.Repository.EmployeeRepository;
import Reentry.first.Repository.ManagerRepository;
import Reentry.first.Repository.TaskAssignmentRepository;
import Reentry.first.Repository.TaskRepository;
import Reentry.first.Repository.WorkGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DemoDataLoader implements CommandLineRunner {

    private final ManagerRepository managerRepository;
    private final WorkGroupRepository workGroupRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;

    @Override
    public void run(String... args) {

        if (taskAssignmentRepository.count() > 0) {
            return;
        }

        Manager manager = new Manager();
        manager.setName("Demo Manager");
        managerRepository.save(manager);

        WorkGroup workGroup = new WorkGroup();
        workGroup.setName("Demo Workgroup");
        workGroup.setManager(manager);
        workGroupRepository.save(workGroup);

        Employee employee1 = new Employee();
        employee1.setName("Ahmed");
        employee1.setLanguage(Employee.Language.Arabic);
        employee1.setWorkGroup(workGroup);

        Employee employee2 = new Employee();
        employee2.setName("Murat");
        employee2.setLanguage(Employee.Language.Turkish);
        employee2.setWorkGroup(workGroup);

        Employee employee3 = new Employee();
        employee3.setName("Anna");
        employee3.setLanguage(Employee.Language.Polish);
        employee3.setWorkGroup(workGroup);

        Employee employee4 = new Employee();
        employee4.setName("Fatima");
        employee4.setLanguage(Employee.Language.Arabic);
        employee4.setWorkGroup(workGroup);

        Employee employee5 = new Employee();
        employee5.setName("Ayse");
        employee5.setLanguage(Employee.Language.Turkish);
        employee5.setWorkGroup(workGroup);

        Employee employee6 = new Employee();
        employee6.setName("Kasia");
        employee6.setLanguage(Employee.Language.Polish);
        employee6.setWorkGroup(workGroup);

        employeeRepository.saveAll(List.of(
                employee1,
                employee2,
                employee3,
                employee4,
                employee5,
                employee6
        ));

        Task task1 = new Task();
        task1.setTitle("Prepare onboarding document");
        task1.setMessage("Create a simple onboarding document for new employees.");
        task1.setCreatedDateTask(LocalDate.now());
        task1.setManager(manager);

        Task task2 = new Task();
        task2.setTitle("Translate safety instructions");
        task2.setMessage("Translate the safety instructions for the workgroup.");
        task2.setCreatedDateTask(LocalDate.now());
        task2.setManager(manager);

        taskRepository.saveAll(List.of(task1, task2));

        TaskAssignment assignment1 = new TaskAssignment();
        assignment1.setName("Arabic onboarding translation");
        assignment1.setDeadline(LocalDate.now().plusDays(7));
        assignment1.setHoursWorked(0);
        assignment1.setStatus(TaskAssignment.Status.TODO);
        assignment1.setTask(task1);
        assignment1.setEmployee(employee1);

        TaskAssignment assignment2 = new TaskAssignment();
        assignment2.setName("Turkish safety translation");
        assignment2.setDeadline(LocalDate.now().plusDays(10));
        assignment2.setHoursWorked(2);
        assignment2.setStatus(TaskAssignment.Status.IN_PROGRESS);
        assignment2.setTask(task2);
        assignment2.setEmployee(employee2);

        TaskAssignment assignment3 = new TaskAssignment();
        assignment3.setName("Polish onboarding review");
        assignment3.setDeadline(LocalDate.now().plusDays(5));
        assignment3.setHoursWorked(4);
        assignment3.setStatus(TaskAssignment.Status.DONE);
        assignment3.setTask(task1);
        assignment3.setEmployee(employee3);

        taskAssignmentRepository.saveAll(List.of(
                assignment1,
                assignment2,
                assignment3
        ));

        System.out.println("Demo data inserted successfully.");
    }
}