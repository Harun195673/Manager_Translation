package ServiceTests.DTO.TranslationDTO;

import lombok.Getter;
import lombok.Setter;
import ServiceTests.Entity.Employee;

@Getter
@Setter
public class RequestTranslationDTO {

    private String text;

    private Employee.Language sourceLang;

    private Employee.Language targetLang;
}