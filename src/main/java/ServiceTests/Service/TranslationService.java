package ServiceTests.Service;

import ServiceTests.Entity.Employee;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class TranslationService {

    private static final String TRANSLATION_URL =
            "https://api.mymemory.translated.net/get";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public TranslationService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String translateText(
            String text,
            Employee.Language sourceLang,
            Employee.Language targetLang
    ) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Text to translate must not be empty"
            );
        }

        if (sourceLang == null || targetLang == null) {
            throw new IllegalArgumentException(
                    "Source and target language are required"
            );
        }


        if (sourceLang == targetLang) {
            return text;
        }

        try {
            String sourceCode =
                    getLanguageCode(sourceLang);

            String targetCode =
                    getLanguageCode(targetLang);

           
            URI uri = UriComponentsBuilder
                    .fromUriString(TRANSLATION_URL)
                    .queryParam("q", text)
                    .queryParam(
                            "langpair",
                            sourceCode + "|" + targetCode
                    )
                    .build()
                    .encode()
                    .toUri();

            ResponseEntity<String> response =
                    restTemplate.getForEntity(
                            uri,
                            String.class
                    );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException(
                        "Translation request failed with status: "
                                + response.getStatusCode()
                );
            }

            if (response.getBody() == null) {
                throw new RuntimeException(
                        "Translation API returned an empty response"
                );
            }

            JsonNode root =
                    objectMapper.readTree(
                            response.getBody()
                    );

            String translatedText = root
                    .path("responseData")
                    .path("translatedText")
                    .asText();

            if (
                    translatedText == null ||
                            translatedText.isBlank()
            ) {
                throw new RuntimeException(
                        "Translation API returned no translated text"
                );
            }

            return translatedText;

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Failed to translate text from "
                            + sourceLang
                            + " to "
                            + targetLang,
                    exception
            );
        }
    }

    private String getLanguageCode(
            Employee.Language language
    ) {

        return switch (language) {
            case GERMAN -> "de";
            case ENGLISH -> "en";
            case TURKISH -> "tr";
            case ARABIC -> "ar";
            case POLISH -> "pl";
            case RUSSIAN -> "ru";
            case SPANISH -> "es";
            case FRENCH -> "fr";
        };
    }
}