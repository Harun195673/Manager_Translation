package ServiceTests;

import ServiceTests.DTO.ManagerDTO.RequestWorkFlowDTO;
import ServiceTests.DTO.TaskAssignmentDTO.RequestTaskAssignmentDTO;
import ServiceTests.DTO.TaskAssignmentDTO.RespondTaskAssignmentDTO;
import ServiceTests.DTO.TaskAssignmentDTO.TaskAssignmentMapper;
import ServiceTests.DTO.TaskDTO.TaskMapper;
import ServiceTests.DTO.WorkgroupDTO.WorkGroupMapper;
import ServiceTests.Entity.Employee;
import ServiceTests.Entity.Manager;
import ServiceTests.Entity.Task;
import ServiceTests.Entity.WorkGroup;
import ServiceTests.Exceptions.InvalidOperationException;
import ServiceTests.Repository.ManagerRepository;
import ServiceTests.Repository.TaskRepository;
import ServiceTests.Repository.WorkGroupRepository;
import ServiceTests.Service.ManagerWorkflowService;
import ServiceTests.Service.TaskAssignmentService;
import ServiceTests.Service.TranslationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerWorkflowServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private WorkGroupRepository workGroupRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private WorkGroupMapper workGroupMapper;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskAssignmentMapper taskAssignmentMapper;

    @Mock
    private TaskAssignmentService taskAssignmentService;

    @Mock
    private TranslationService translationService;

    @InjectMocks
    private ManagerWorkflowService managerWorkflowService;

    @Test
    void taskTranslationWorkflow_shouldCreateAssignmentsForEmployees() {

        RequestWorkFlowDTO dto = mock(RequestWorkFlowDTO.class);
        when(dto.getManagerId()).thenReturn(1L);
        when(dto.getWorkGroupId()).thenReturn(10L);
        when(dto.getTaskId()).thenReturn(100L);

        Manager manager = mock(Manager.class);
        WorkGroup workGroup = mock(WorkGroup.class);
        Task originalTask = mock(Task.class);
        Task translatedTask = mock(Task.class);
        Employee employee = mock(Employee.class);

        RequestTaskAssignmentDTO requestDto = mock(RequestTaskAssignmentDTO.class);
        RespondTaskAssignmentDTO responseDto = mock(RespondTaskAssignmentDTO.class);

        when(manager.getId()).thenReturn(1L);
        when(workGroup.getManager()).thenReturn(manager);
        when(workGroup.getEmployeeList()).thenReturn(List.of(employee));
        when(originalTask.getMessage()).thenReturn("Prepare onboarding document");

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(workGroupRepository.findById(10L)).thenReturn(Optional.of(workGroup));
        when(taskRepository.findById(100L)).thenReturn(Optional.of(originalTask));

        HashMap<Employee.Language, List<Employee>> employeesByLanguage = new HashMap<>();
        employeesByLanguage.put(Employee.Language.TURKISH, List.of(employee));

        when(workGroupMapper.getEmployeeMap(List.of(employee)))
                .thenReturn(employeesByLanguage);

        when(translationService.translateText(
                "Prepare onboarding document",
                Employee.Language.GERMAN,
                Employee.Language.TURKISH
        )).thenReturn("Translated message");

        when(taskMapper.createTranslatedTask(originalTask, "Translated message"))
                .thenReturn(translatedTask);

        when(taskRepository.save(translatedTask)).thenReturn(translatedTask);

        when(taskAssignmentMapper.buildTaskAssignmentRequest(
                dto,
                employee,
                translatedTask,
                Employee.Language.TURKISH
        )).thenReturn(requestDto);

        when(taskAssignmentService.assignTaskToEmployee(requestDto))
                .thenReturn(responseDto);

        List<RespondTaskAssignmentDTO> result =
                managerWorkflowService.taskTranslationWorkflow(dto);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));

        verify(translationService).translateText(
                "Prepare onboarding document",
                Employee.Language.GERMAN,
                Employee.Language.TURKISH
        );

        verify(taskAssignmentService).assignTaskToEmployee(requestDto);
    }

    @Test
    void taskTranslationWorkflow_shouldThrowException_whenManagerDoesNotOwnWorkGroup() {

        RequestWorkFlowDTO dto = mock(RequestWorkFlowDTO.class);
        when(dto.getManagerId()).thenReturn(1L);
        when(dto.getWorkGroupId()).thenReturn(10L);
        when(dto.getTaskId()).thenReturn(100L);

        Manager manager = mock(Manager.class);
        Manager otherManager = mock(Manager.class);
        WorkGroup workGroup = mock(WorkGroup.class);
        Task task = mock(Task.class);

        when(manager.getId()).thenReturn(1L);
        when(otherManager.getId()).thenReturn(99L);
        when(workGroup.getManager()).thenReturn(otherManager);

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(workGroupRepository.findById(10L)).thenReturn(Optional.of(workGroup));
        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));

        assertThrows(InvalidOperationException.class, () ->
                managerWorkflowService.taskTranslationWorkflow(dto)
        );

        verifyNoInteractions(translationService);
        verifyNoInteractions(taskAssignmentService);
    }
}