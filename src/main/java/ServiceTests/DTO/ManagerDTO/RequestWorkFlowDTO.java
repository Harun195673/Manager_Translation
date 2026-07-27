package ServiceTests.DTO.ManagerDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class RequestWorkFlowDTO {

    @NotNull
    Long managerId;
    @NotNull
    Long workGroupId;
    @NotNull
    Long taskId;
    @DateTimeFormat
    LocalDate deadline;
    @NotNull
    String taskAssignmentName;
}
