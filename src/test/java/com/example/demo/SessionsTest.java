package com.example.demo;

import com.example.demo.config.WebSecurityConfig;
import com.example.demo.controller.JwtController;
import com.example.demo.security.JwtAuthenticationEntryPoint;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.service.AuthService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(
//        value = JwtController.class
//)
public class SessionsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

//    protected MockHttpSession session;

//    protected MockHttpServletRequest request;

    private static final String username = "user";
    private static final String password = "useruser";
    private static final String loginBoby = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

//    @PostConstruct
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup() MockMvcBuilders.webAppContextSetup(this.wac).addFilters(this.springSecurityFilterChain).build();
//    }

    @Test
    void test() throws Exception {
        // I log in and then returns session
        List<HttpSession> sessions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            HttpSession session = mockMvc.perform(post("/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginBoby))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getRequest()
                    .getSession();

            sessions.add(session);
            Thread.sleep(1);
        }
        System.out.println();
    }
}
