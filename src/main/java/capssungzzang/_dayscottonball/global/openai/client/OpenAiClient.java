package capssungzzang._dayscottonball.global.openai.client;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAiClient {
    private final WebClient client;
    @Value("${spring.ai.openai.chat.options.model}") private String model;

    public String chatJson(String userPrompt) {
        ChatRequest request = new ChatRequest(model,
                List.of(new Message("system","You return ONLY valid JSON."), new Message("user", userPrompt)),
                0.7, new ResponseFormat("json_object"));

        ChatResponse response = client.post().uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        if (response == null || response.choices == null || response.choices.isEmpty())
            throw new IllegalStateException("OpenAI empty response");
        return response.choices.get(0).message.content;
    }

    public record ChatRequest(String model, List<Message> messages, Double temperature, ResponseFormat response_format) {}
    public record ResponseFormat(String type) {}
    public record Message(String role, String content) {}
    public record ChatResponse(List<Choice> choices) {}
    public record Choice(Message message) {}
}

