package dev.ivanvoloshyn.redriveapi.auth;

import dev.ivanvoloshyn.redriveapi.user.UserService;
import dev.ivanvoloshyn.redriveapi.user.model.RegisterUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_shouldThrownMethodArgumentNotValidException_thenEmailIsNotValid() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest(
                "User",
                "UserLastName",
                "wrong_email@",
                "user_password"
        );
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().string(containsString("Email is not valid")));
    }

    @Test
    void register_shouldThrownMethodArgumentNotValidException_thenEmailIsBlank() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest(
                "User",
                "UserLastName",
                "",
                "user_password"
        );
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().string(containsString("Email is required")));
    }

}
