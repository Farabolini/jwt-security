package br.com.marcello.Security.integration;

import br.com.marcello.Security.dto.AccountDto;
import br.com.marcello.Security.service.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class LoginControllerTest extends AbstractDatabaseIntegration {

    String URL_PATH = "/login";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SessionService sessionService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldPerformPostLoginRequest() throws Exception {
        AccountDto accountDto = new AccountDto().toBuilder()
                .username("adminTest")
                .password("teste10")
                .build();

        this.mockMvc.perform(post(URL_PATH)
                    .header(HttpHeaders.AUTHORIZATION, this.sessionService.login(accountDto.getUsername(), accountDto.getPassword()).getToken())
                    .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(accountDto)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("adminTest"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

}
