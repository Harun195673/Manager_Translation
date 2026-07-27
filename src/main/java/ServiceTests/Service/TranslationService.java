package management_workflow_api.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import management_workflow_api.Entity.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TranslationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String translateText(String text, Employee.Language sourceLang, Employee.Language targetLang) {

        try {
            String sourceCode;
            String targetCode;

            if (sourceLang == Employee.Language.GERMAN) {
                sourceCode = "de";
            } else if (sourceLang == Employee.Language.ENGLISH) {
                sourceCode = "en";
            } else if (sourceLang == Employee.Language.TURKISH) {
                sourceCode = "tr";
            } else if (sourceLang == Employee.Language.ARABIC) {
                sourceCode = "ar";
            } else if (sourceLang == Employee.Language.POLISH) {
                sourceCode = "pl";
            } else {
                throw new RuntimeException("Unsupported source language: " + sourceLang);
            }

            if (targetLang == Employee.Language.GERMAN) {
                targetCode = "de";
            } else if (targetLang == Employee.Language.ENGLISH) {
                targetCode = "en";
            } else if (targetLang == Employee.Language.TURKISH) {
                targetCode = "tr";
            } else if (targetLang == Employee.Language.ARABIC) {
                targetCode = "ar";
            } else if (targetLang == Employee.Language.POLISH) {
                targetCode = "pl";
            } else {
                throw new RuntimeException("Unsupported target language: " + targetLang);
            }

            /// Encode text to be URL-safe
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

            /// Build URL
            String url = String.format(
                    "https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s",
                    encodedText, sourceCode, targetCode
            );

            /// Make GET request
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("responseData").path("translatedText").asText();
            } else {
                throw new RuntimeException("Translation request failed: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to translate text", e);
        }
    }
}