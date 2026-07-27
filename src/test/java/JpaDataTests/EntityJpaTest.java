package JpaDataTests;

import ServiceTests.Entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
        excludeAutoConfiguration = JpaRepositoriesAutoConfiguration.class
)
class EntityJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void manager_shouldCascadePersistWorkGroupsAndTasks() {
        Manager manager = new Manager();
        manager.setName("John");
        manager.setWorkGroupList(new ArrayList<>());
        manager.setTaskList(new ArrayList<>());

        WorkGroup workGroup = new WorkGroup();
        workGroup.setName("Backend Team");
        workGroup.setManager(manager);

        Task task = new Task();
        task.setTitle("Create API");
        task.setMessage("Create the employee API");
        task.setCreatedDateTask(LocalDate.now());
        task.setManager(manager);

        manager.getWorkGroupList().add(workGroup);
        manager.getTaskList().add(task);

        entityManager.persistAndFlush(manager);

        assertThat(manager.getId()).isNotNull();
        assertThat(workGroup.getId()).isNotNull();
        assertThat(task.getId()).isNotNull();
    }

    @Test
    void workGroup_shouldCascadePersistEmployees() {
        Manager manager = createAndPersistManager();

        WorkGroup workGroup = new WorkGroup();
        workGroup.setName("Translation Team");
        workGroup.setManager(manager);
        workGroup.setEmployeeList(new ArrayList<>());

        Employee employee = new Employee();
        employee.setName("Anna");
        employee.setLanguage(Employee.Language.ENGLISH);
        employee.setWorkGroup(workGroup);

        workGroup.getEmployeeList().add(employee);

        entityManager.persistAndFlush(workGroup);

        assertThat(workGroup.getId()).isNotNull();
        assertThat(employee.getId()).isNotNull();

        Employee savedEmployee =
                entityManager.find(Employee.class, employee.getId());

        assertThat(savedEmployee.getName()).isEqualTo("Anna");
        assertThat(savedEmployee.getWorkGroup().getId())
                .isEqualTo(workGroup.getId());
    }

    @Test
    void employee_shouldCascadePersistWebUser() {
        Manager manager = createAndPersistManager();
        WorkGroup workGroup = createAndPersistWorkGroup(manager);

        WebUser webUser = new WebUser();
        webUser.setUsername("anna@example.com");
        webUser.setPassword("encoded-password");
        webUser.setRole("EMPLOYEE");

        Employee employee = new Employee();
        employee.setName("Anna");
        employee.setLanguage(Employee.Language.ENGLISH);
        employee.setWorkGroup(workGroup);
        employee.setWebUser(webUser);

        webUser.setEmployee(employee);

        entityManager.persistAndFlush(employee);

        assertThat(employee.getId()).isNotNull();
        assertThat(webUser.getId()).isNotNull();

        entityManager.clear();

        Employee savedEmployee =
                entityManager.find(Employee.class, employee.getId());

        assertThat(savedEmployee.getWebUser()).isNotNull();
        assertThat(savedEmployee.getWebUser().getUsername())
                .isEqualTo("anna@example.com");
    }

    @Test
    void manager_shouldCascadePersistWebUser() {
        WebUser webUser = new WebUser();
        webUser.setUsername("manager@example.com");
        webUser.setPassword("encoded-password");
        webUser.setRole("MANAGER");

        Manager manager = new Manager();
        manager.setName("Maria");
        manager.setWebUser(webUser);

        webUser.setManager(manager);

        entityManager.persistAndFlush(manager);

        assertThat(manager.getId()).isNotNull();
        assertThat(webUser.getId()).isNotNull();

        entityManager.clear();

        Manager savedManager =
                entityManager.find(Manager.class, manager.getId());

        assertThat(savedManager.getWebUser()).isNotNull();
        assertThat(savedManager.getWebUser().getUsername())
                .isEqualTo("manager@example.com");
    }

    @Test
    void removingEmployeeFromWorkGroup_shouldDeleteEmployee() {
        Manager manager = createAndPersistManager();

        WorkGroup workGroup = new WorkGroup();
        workGroup.setName("Backend Team");
        workGroup.setManager(manager);
        workGroup.setEmployeeList(new ArrayList<>());

        Employee employee = new Employee();
        employee.setName("Anna");
        employee.setLanguage(Employee.Language.ENGLISH);
        employee.setWorkGroup(workGroup);

        workGroup.getEmployeeList().add(employee);

        entityManager.persistAndFlush(workGroup);

        Long workGroupId = workGroup.getId();
        Long employeeId = employee.getId();

        entityManager.clear();

        WorkGroup savedWorkGroup =
                entityManager.find(WorkGroup.class, workGroupId);

        Employee employeeToRemove =
                savedWorkGroup.getEmployeeList().get(0);

        savedWorkGroup.getEmployeeList().remove(employeeToRemove);
        employeeToRemove.setWorkGroup(null);

        entityManager.flush();
        entityManager.clear();

        Employee deletedEmployee =
                entityManager.find(Employee.class, employeeId);

        assertThat(deletedEmployee).isNull();
    }

    @Test
    void removingWorkGroupFromManager_shouldDeleteWorkGroup() {
        Manager manager = new Manager();
        manager.setName("Maria");
        manager.setWorkGroupList(new ArrayList<>());
        manager.setTaskList(new ArrayList<>());

        WorkGroup workGroup = new WorkGroup();
        workGroup.setName("Backend Team");
        workGroup.setManager(manager);

        manager.getWorkGroupList().add(workGroup);

        entityManager.persistAndFlush(manager);

        Long managerId = manager.getId();
        Long workGroupId = workGroup.getId();

        entityManager.clear();

        Manager savedManager =
                entityManager.find(Manager.class, managerId);

        WorkGroup groupToRemove =
                savedManager.getWorkGroupList().get(0);

        savedManager.getWorkGroupList().remove(groupToRemove);
        groupToRemove.setManager(null);

        entityManager.flush();
        entityManager.clear();

        WorkGroup deletedWorkGroup =
                entityManager.find(WorkGroup.class, workGroupId);

        assertThat(deletedWorkGroup).isNull();
    }

    @Test
    void removingWebUserFromEmployee_shouldDeleteWebUser() {
        Manager manager = createAndPersistManager();
        WorkGroup workGroup = createAndPersistWorkGroup(manager);

        WebUser webUser = new WebUser();
        webUser.setUsername("employee@example.com");
        webUser.setPassword("encoded-password");
        webUser.setRole("EMPLOYEE");

        Employee employee = new Employee();
        employee.setName("Anna");
        employee.setLanguage(Employee.Language.ENGLISH);
        employee.setWorkGroup(workGroup);
        employee.setWebUser(webUser);

        webUser.setEmployee(employee);

        entityManager.persistAndFlush(employee);

        Long employeeId = employee.getId();
        Long webUserId = webUser.getId();

        entityManager.clear();

        Employee savedEmployee =
                entityManager.find(Employee.class, employeeId);

        WebUser savedWebUser = savedEmployee.getWebUser();

        savedEmployee.setWebUser(null);
        savedWebUser.setEmployee(null);

        entityManager.flush();
        entityManager.clear();

        WebUser deletedWebUser =
                entityManager.find(WebUser.class, webUserId);

        assertThat(deletedWebUser).isNull();
    }

    @Test
    void taskAssignment_shouldPersistTaskAndEmployeeRelationships() {
        Manager manager = createAndPersistManager();
        WorkGroup workGroup = createAndPersistWorkGroup(manager);

        Employee employee = createAndPersistEmployee(
                workGroup,
                "Anna",
                Employee.Language.ENGLISH
        );

        Task task = createAndPersistTask(manager);

        TaskAssignment assignment = new TaskAssignment();
        assignment.setName("English translation");
        assignment.setDeadline(LocalDate.now().plusDays(7));
        assignment.setHoursWorked(2);
        assignment.setTranslatedText("Translated text");
        assignment.setStatus(TaskAssignment.Status.IN_PROGRESS);
        assignment.setTask(task);
        assignment.setEmployee(employee);

        entityManager.persistAndFlush(assignment);

        Long assignmentId = assignment.getId();

        entityManager.clear();

        TaskAssignment savedAssignment =
                entityManager.find(TaskAssignment.class, assignmentId);

        assertThat(savedAssignment.getTask().getId())
                .isEqualTo(task.getId());

        assertThat(savedAssignment.getEmployee().getId())
                .isEqualTo(employee.getId());

        assertThat(savedAssignment.getStatus())
                .isEqualTo(TaskAssignment.Status.IN_PROGRESS);
    }

    @Test
    void deletingTask_shouldDeleteTaskAssignments() {
        Manager manager = createAndPersistManager();
        WorkGroup workGroup = createAndPersistWorkGroup(manager);

        Employee employee = createAndPersistEmployee(
                workGroup,
                "Anna",
                Employee.Language.ENGLISH
        );

        Task task = createAndPersistTask(manager);

        TaskAssignment assignment = new TaskAssignment();
        assignment.setName("Translate document");
        assignment.setDeadline(LocalDate.now().plusDays(5));
        assignment.setStatus(TaskAssignment.Status.TODO);
        assignment.setTask(task);
        assignment.setEmployee(employee);

        entityManager.persistAndFlush(assignment);

        Long taskId = task.getId();
        Long assignmentId = assignment.getId();

        entityManager.clear();

        Task savedTask = entityManager.find(Task.class, taskId);

        entityManager.remove(savedTask);
        entityManager.flush();
        entityManager.clear();

        TaskAssignment deletedAssignment =
                entityManager.find(TaskAssignment.class, assignmentId);

        assertThat(deletedAssignment).isNull();
    }

    @Test
    void deletingEmployee_shouldDeleteTaskAssignments() {
        Manager manager = createAndPersistManager();
        WorkGroup workGroup = createAndPersistWorkGroup(manager);

        Employee employee = createAndPersistEmployee(
                workGroup,
                "Anna",
                Employee.Language.ENGLISH
        );

        Task task = createAndPersistTask(manager);

        TaskAssignment assignment = new TaskAssignment();
        assignment.setName("Translate document");
        assignment.setDeadline(LocalDate.now().plusDays(5));
        assignment.setStatus(TaskAssignment.Status.TODO);
        assignment.setTask(task);
        assignment.setEmployee(employee);

        entityManager.persistAndFlush(assignment);

        Long employeeId = employee.getId();
        Long assignmentId = assignment.getId();

        entityManager.clear();

        Employee savedEmployee =
                entityManager.find(Employee.class, employeeId);

        entityManager.remove(savedEmployee);
        entityManager.flush();
        entityManager.clear();

        TaskAssignment deletedAssignment =
                entityManager.find(TaskAssignment.class, assignmentId);

        assertThat(deletedAssignment).isNull();
    }

    @Test
    void employeeLanguage_shouldBePersistedAsEnum() {
        Manager manager = createAndPersistManager();
        WorkGroup workGroup = createAndPersistWorkGroup(manager);

        Employee employee = createAndPersistEmployee(
                workGroup,
                "Mehmet",
                Employee.Language.TURKISH
        );

        Long employeeId = employee.getId();

        entityManager.clear();

        Employee savedEmployee =
                entityManager.find(Employee.class, employeeId);

        assertThat(savedEmployee.getLanguage())
                .isEqualTo(Employee.Language.TURKISH);
    }

    @Test
    void taskAssignmentStatus_shouldBePersistedAsEnum() {
        Manager manager = createAndPersistManager();
        WorkGroup workGroup = createAndPersistWorkGroup(manager);

        Employee employee = createAndPersistEmployee(
                workGroup,
                "Anna",
                Employee.Language.ENGLISH
        );

        Task task = createAndPersistTask(manager);

        TaskAssignment assignment = new TaskAssignment();
        assignment.setName("Translate document");
        assignment.setDeadline(LocalDate.now().plusDays(5));
        assignment.setStatus(TaskAssignment.Status.OVERDUE);
        assignment.setTask(task);
        assignment.setEmployee(employee);

        entityManager.persistAndFlush(assignment);

        Long assignmentId = assignment.getId();

        entityManager.clear();

        TaskAssignment savedAssignment =
                entityManager.find(TaskAssignment.class, assignmentId);

        assertThat(savedAssignment.getStatus())
                .isEqualTo(TaskAssignment.Status.OVERDUE);
    }

    private Manager createAndPersistManager() {
        Manager manager = new Manager();
        manager.setName("Manager");
        manager.setWorkGroupList(new ArrayList<>());
        manager.setTaskList(new ArrayList<>());

        return entityManager.persistAndFlush(manager);
    }

    private WorkGroup createAndPersistWorkGroup(Manager manager) {
        WorkGroup workGroup = new WorkGroup();
        workGroup.setName("Backend Team");
        workGroup.setManager(manager);
        workGroup.setEmployeeList(new ArrayList<>());

        return entityManager.persistAndFlush(workGroup);
    }

    private Employee createAndPersistEmployee(
            WorkGroup workGroup,
            String name,
            Employee.Language language
    ) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setLanguage(language);
        employee.setWorkGroup(workGroup);
        employee.setTaskAssignmentList(new ArrayList<>());

        return entityManager.persistAndFlush(employee);
    }

    private Task createAndPersistTask(Manager manager) {
        Task task = new Task();
        task.setTitle("Create API");
        task.setMessage("Create the employee API");
        task.setCreatedDateTask(LocalDate.now());
        task.setManager(manager);
        task.setTaskAssignmentList(new ArrayList<>());

        return entityManager.persistAndFlush(task);
    }
}