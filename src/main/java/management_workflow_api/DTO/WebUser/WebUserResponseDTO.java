package management_workflow_api.DTO.WebUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WebUserResponseDTO {
    private Long id;
    private String username;
    private String role;
}
