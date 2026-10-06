package fr.miage.tp4.chat;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!foundry")
public class TrainingChatAgentService implements ChatAgentService {

    @Override
    public ChatResponse chat(ChatRequest request) {
        String conversationId = request.conversationId() == null || request.conversationId().isBlank()
                ? UUID.randomUUID().toString()
                : request.conversationId();

        return new ChatResponse(
                "Connexion a Microsoft Foundry a implementer pendant le TP.",
                conversationId);
    }
}