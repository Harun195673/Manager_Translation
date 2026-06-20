package management_workflow_api.Controller;

import jakarta.validation.Valid;
import management_workflow_api.DTO.TranslationDTO.RequestTranslationDTO;
import management_workflow_api.Service.TranslationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/translate")
public class TranslationController {

    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    public String translateText(@RequestBody @Valid RequestTranslationDTO request) {
        return translationService.translateText(
                request.getText(),
                request.getSourceLang(),
                request.getTargetLang()
        );
    }
}