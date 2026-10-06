package fr.miage.tp4.chat;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TrainingChatAgentServiceTest {

    private final TrainingChatAgentService service = new TrainingChatAgentService();

    @Test
    void createsConversationIdForFirstMessage() {
        ChatResponse response = service.chat(new ChatRequest("Bonjour", null));

        assertThat(response.conversationId()).isNotBlank();
    }

    @Test
    void keepsConversationIdForFollowUpMessage() {
        ChatResponse response = service.chat(new ChatRequest("Et ensuite ?", "conv-456"));

        assertThat(response.conversationId()).isEqualTo("conv-456");
    }
}