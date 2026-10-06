package fr.miage.tp4.chat;

import java.util.stream.Collectors;

import com.azure.ai.agents.AgentsClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.openai.client.OpenAIClient;
import com.openai.models.conversations.Conversation;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("foundry")
public class FoundryChatAgentService implements ChatAgentService {

    private final OpenAIClient openAIClient;

    public FoundryChatAgentService(
            @Value("${azure.ai.project-endpoint}") String projectEndpoint,
            @Value("${azure.ai.agent-name}") String agentName) {
        openAIClient = new AgentsClientBuilder()
                .credential(new DefaultAzureCredentialBuilder().build())
                .endpoint(projectEndpoint)
                .buildAgentScopedOpenAIClient(agentName);
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        String conversationId = request.conversationId();
        if (conversationId == null || conversationId.isBlank()) {
            Conversation conversation = openAIClient.conversations().create();
            conversationId = conversation.id();
        }

        Response response = openAIClient.responses().create(
                ResponseCreateParams.builder()
                        .conversation(conversationId)
                        .input(request.message())
                        .build());

        String answer = response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(outputText -> outputText.text())
                .collect(Collectors.joining("\n"));

        if (answer.isBlank()) {
            throw new IllegalStateException("Microsoft Foundry a retourne une reponse vide");
        }

        return new ChatResponse(answer, conversationId);
    }
}