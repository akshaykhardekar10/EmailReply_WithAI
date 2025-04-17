package com.emailSender.EmailSender.EmailService;

import com.emailSender.EmailSender.EmailDTO.EmailDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    WebClient webClient = WebClient.builder().build();

    public String generateEmail(EmailDTO mailRequest) {

        // generating prompt
        String prompt = buildPrompt(mailRequest);

        // Generating structure for API
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                }
        );

        // get response

        String response = webClient.post()
                .uri(geminiApiUrl +"?key="+ geminiApiKey)
                .header("Content-Type" ,"application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //return response

        return extracContent(response);
    }

    private String extracContent(String response) {
        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode rootNode = mapper.readTree(response);

            return rootNode.path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();

        }catch (Exception e){
            return "error in response" + e.getMessage();
        }
    }

    private String buildPrompt(EmailDTO mailRequest) {
        StringBuilder provide = new StringBuilder();
        provide.append("Generate an email reply for the following mail content. Please don't generate a subject line. ");
        if (mailRequest.getTone() != null && !mailRequest.getTone().isEmpty()) {
            provide.append("Use a ").append(mailRequest.getTone()).append(" tone. ");
        }
        provide.append("\nOriginal mail: ").append(mailRequest.getEmailContent());
        return provide.toString();
    }
}
