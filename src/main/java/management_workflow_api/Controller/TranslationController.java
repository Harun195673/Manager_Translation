package management_workflow_api.Controller;

import management_workflow_api.Service.TranslationService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/translate")
public class TranslationController {

    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    public String translateText(@RequestBody @Valid TranslationRequest request) {
        return translationService.translateText(
                request.getText(),
                request.getSourceLang(),
                request.getTargetLang()
        );
    }

    @Getter
    @Setter
    public static class TranslationRequest {
        private String text;
        private String sourceLang; // e.g., "en"
        private String targetLang; // e.g., "de"

        public TranslationRequest() {} // for Jackson
    }
}