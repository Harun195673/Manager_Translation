package management_workflow_api.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TranslationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();




    public String translateText(String text, String sourceLang, String targetLang) {
        try {
            /// Encode text to be URL-safe
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

            /// Build URL
            String url = String.format(
                    "https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s",
                    encodedText, sourceLang, targetLang
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