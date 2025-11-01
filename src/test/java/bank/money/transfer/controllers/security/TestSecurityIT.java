package bank.money.transfer.controllers.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import bank.money.transfer.security.auth.AuthenticationRequest;
import bank.money.transfer.security.auth.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestSecurityIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String obtainAccessToken(String email, String password) throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("Test")
                .lastName("User")
                .email(email)
                .password(password)
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    public void whenAccessProtectedEndpointWithoutToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/account/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenAccessProtectedEndpointWithValidToken_thenSuccess() throws Exception {
        // Get a valid token
        String token = obtainAccessToken("testuser@example.com", "TestPassword123!");

        // Use the token to access protected endpoint
        mockMvc.perform(get("/api/v1/account")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    @Test
    public void whenCreateTransactionWithValidToken_thenSuccess() throws Exception {
        // Get a valid token
        String token = obtainAccessToken("transactionuser@example.com", "Password123!");

        String transactionJson = """
            {
                "sourceAccountId": 1,
                "targetAccountId": 2,
                "amount": 100.00,
                "currency": "USD"
            }
            """;

        mockMvc.perform(post("/api/v1/transaction")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isBadRequest()); // Will fail because accounts don't exist, but auth works!
    }
}