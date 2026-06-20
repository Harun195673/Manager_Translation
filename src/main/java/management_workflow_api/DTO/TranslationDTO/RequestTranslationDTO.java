package management_workflow_api.DTO.TranslationDTO;

import lombok.Getter;
import lombok.Setter;
import management_workflow_api.Entity.Employee;

@Getter
@Setter
public class RequestTranslationDTO {

    private String text;

    private Employee.Language sourceLang;

    private Employee.Language targetLang;
}