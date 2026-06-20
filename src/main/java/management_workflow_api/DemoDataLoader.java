package management_workflow_api;

import management_workflow_api.Entity.Employee;
import management_workflow_api.Entity.Manager;
import management_workflow_api.Entity.Task;
import management_workflow_api.Entity.TaskAssignment;
import management_workflow_api.Entity.WebUser;
import management_workflow_api.Entity.WorkGroup;
import management_workflow_api.Repository.EmployeeRepository;
import management_workflow_api.Repository.ManagerRepository;
import management_workflow_api.Repository.TaskAssignmentRepository;
import management_workflow_api.Repository.TaskRepository;
import management_workflow_api.Repository.WorkGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (taskAssignmentRepository.count() > 0) {
            return;
        }

        // -------------------------
        // Manager + corresponding WebUser
        // -------------------------

        WebUser managerUser = new WebUser();
        managerUser.setUsername("manager");
        managerUser.setPassword(passwordEncoder.encode("password"));
        managerUser.setRole("MANAGER");

        Manager manager = new Manager();
        manager.setName("Demo Manager");
        manager.setWebUser(managerUser);

        managerUser.setManager(manager);

        managerRepository.save(manager);

        // -------------------------
        // WorkGroup
        // -------------------------

        WorkGroup workGroup = new WorkGroup();
        workGroup.setName("Demo Workgroup");
        workGroup.setManager(manager);

        workGroupRepository.save(workGroup);

        // -------------------------
        // Employee 1 + corresponding WebUser
        // -------------------------

        WebUser employee1User = new WebUser();
        employee1User.setUsername("ahmed");
        employee1User.setPassword(passwordEncoder.encode("password"));
        employee1User.setRole("EMPLOYEE");

        Employee employee1 = new Employee();
        employee1.setName("Ahmed");
        employee1.setLanguage(Employee.Language.ARABIC);
        employee1.setWorkGroup(workGroup);
        employee1.setWebUser(employee1User);

        employee1User.setEmployee(employee1);

        // -------------------------
        // Employee 2 + corresponding WebUser
        // -------------------------

        WebUser employee2User = new WebUser();
        employee2User.setUsername("murat");
        employee2User.setPassword(passwordEncoder.encode("password"));
        employee2User.setRole("EMPLOYEE");

        Employee employee2 = new Employee();
        employee2.setName("Murat");
        employee2.setLanguage(Employee.Language.TURKISH);
        employee2.setWorkGroup(workGroup);
        employee2.setWebUser(employee2User);

        employee2User.setEmployee(employee2);

        // -------------------------
        // Employee 3 + corresponding WebUser
        // -------------------------

        WebUser employee3User = new WebUser();
        employee3User.setUsername("anna");
        employee3User.setPassword(passwordEncoder.encode("password"));
        employee3User.setRole("EMPLOYEE");

        Employee employee3 = new Employee();
        employee3.setName("Anna");
        employee3.setLanguage(Employee.Language.POLISH);
        employee3.setWorkGroup(workGroup);
        employee3.setWebUser(employee3User);

        employee3User.setEmployee(employee3);

        // -------------------------
        // Employee 4 + corresponding WebUser
        // -------------------------

        WebUser employee4User = new WebUser();
        employee4User.setUsername("fatima");
        employee4User.setPassword(passwordEncoder.encode("password"));
        employee4User.setRole("EMPLOYEE");

        Employee employee4 = new Employee();
        employee4.setName("Fatima");
        employee4.setLanguage(Employee.Language.ARABIC);
        employee4.setWorkGroup(workGroup);
        employee4.setWebUser(employee4User);

        employee4User.setEmployee(employee4);

        // -------------------------
        // Employee 5 + corresponding WebUser
        // -------------------------

        WebUser employee5User = new WebUser();
        employee5User.setUsername("ayse");
        employee5User.setPassword(passwordEncoder.encode("password"));
        employee5User.setRole("EMPLOYEE");

        Employee employee5 = new Employee();
        employee5.setName("Ayse");
        employee5.setLanguage(Employee.Language.TURKISH);
        employee5.setWorkGroup(workGroup);
        employee5.setWebUser(employee5User);

        employee5User.setEmployee(employee5);

        // -------------------------
        // Employee 6 + corresponding WebUser
        // -------------------------

        WebUser employee6User = new WebUser();
        employee6User.setUsername("kasia");
        employee6User.setPassword(passwordEncoder.encode("password"));
        employee6User.setRole("EMPLOYEE");

        Employee employee6 = new Employee();
        employee6.setName("Kasia");
        employee6.setLanguage(Employee.Language.POLISH);
        employee6.setWorkGroup(workGroup);
        employee6.setWebUser(employee6User);

        employee6User.setEmployee(employee6);

        // Because Employee -> WebUser has CascadeType.ALL,
        // saving the employees also saves their WebUser objects.
        employeeRepository.saveAll(List.of(
                employee1,
                employee2,
                employee3,
                employee4,
                employee5,
                employee6
        ));

        // -------------------------
        // Tasks
        // -------------------------

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

        // -------------------------
        // TaskAssignments
        // -------------------------

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