package ServiceTests.DTO.TaskAssignmentDTO;

import ServiceTests.DTO.ManagerDTO.RequestWorkFlowDTO;
import ServiceTests.Entity.Employee;
import ServiceTests.Entity.Task;
import ServiceTests.Entity.TaskAssignment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskAssignmentMapper {


    public TaskAssignment toEntity (RequestTaskAssignmentDTO dto){

        TaskAssignment taskAssignment = new TaskAssignment();

        taskAssignment.setDeadline(dto.getDeadline());
        taskAssignment.setStatus(TaskAssignment.Status.TODO);
        taskAssignment.setName(dto.getName());
        taskAssignment.setTranslatedText(dto.getTranslatedTask());



        return taskAssignment;
    }





    public RespondTaskAssignmentDTO toDTO (TaskAssignment newtaskAssignment){

        RespondTaskAssignmentDTO dto = new RespondTaskAssignmentDTO();

        dto.setId(newtaskAssignment.getId());
        dto.setTaskTitle(newtaskAssignment.getTask().getTitle());
        dto.setStatus(newtaskAssignment.getStatus());
        dto.setEmployeeName(newtaskAssignment.getEmployee().getName());
        dto.setDeadline(newtaskAssignment.getDeadline());
        dto.setName(newtaskAssignment.getName());
        dto.setCreatedDate(LocalDate.now());
        dto.setTranslatedTask(newtaskAssignment.getTranslatedText());
        return dto;
    }



    public List<RespondTaskAssignmentDTO> toDTOList (List<TaskAssignment> taskAssignmentList){

        List<RespondTaskAssignmentDTO> respondTaskAssignmentDTOList = new ArrayList<>();
        for (TaskAssignment taskAssignment: taskAssignmentList){
            respondTaskAssignmentDTOList.add(this.toDTO(taskAssignment));
        }

        return respondTaskAssignmentDTOList;
    }




    public TaskAssignment updateEntity (TaskAssignment oldTaskAssignment, RequestUpdateTaskAssignmentDTO dto){


        oldTaskAssignment.setId(dto.getNewTaskAssignmentId());
        oldTaskAssignment.setDeadline(dto.getDeadline());
        oldTaskAssignment.setStatus(dto.getStatus());
        oldTaskAssignment.setHoursWorked(dto.getHoursWorked());
        oldTaskAssignment.setName(dto.getName());

        return oldTaskAssignment;
    }





    public RequestTaskAssignmentDTO buildTaskAssignmentRequest(
            RequestWorkFlowDTO workFlowDto,
            Employee employee,
            Task translatedTask,
            Employee.Language newLanguageName
    ) {

        RequestTaskAssignmentDTO taskAssignmentDTO =
                new RequestTaskAssignmentDTO();

        taskAssignmentDTO.setDeadline(workFlowDto.getDeadline());
        taskAssignmentDTO.setEmployeeId(employee.getId());
        taskAssignmentDTO.setTaskId(translatedTask.getId());
        taskAssignmentDTO.setName( "Task name: " + workFlowDto.getTaskAssignmentName()+ " (" + newLanguageName + ")");
        taskAssignmentDTO.setTranslatedTask(translatedTask.getMessage());

        return taskAssignmentDTO;
    }




}
