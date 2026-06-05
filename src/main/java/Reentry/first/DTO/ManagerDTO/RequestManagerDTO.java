package Reentry.first.DTO.ManagerDTO;

import Reentry.first.Entity.WorkGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestManagerDTO {

    @NotBlank(message = "Name required")
    private String name;
}
