package fr.miage.tp4.chat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ChatControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ChatAgentService service = request -> new ChatResponse("Redemarrez le client VPN.", "conv-123");
        mockMvc = MockMvcBuilders.standaloneSetup(new ChatController(service)).build();
    }

    @Test
    void returnsAgentAnswerAndConversationId() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"message":"Comment reparer le VPN ?"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Redemarrez le client VPN."))
                .andExpect(jsonPath("$.conversationId").value("conv-123"));
    }

    @Test
    void rejectsBlankMessage() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"message":"   "}
                                """))
                .andExpect(status().isBadRequest());
    }
}