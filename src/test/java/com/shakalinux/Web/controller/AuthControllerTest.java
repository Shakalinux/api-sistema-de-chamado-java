package com.shakalinux.Web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shakalinux.Web.dto.LoginRequestDTO;
import com.shakalinux.Web.dto.UserCreateDTO;
import com.shakalinux.Web.utils.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginAndTokenGeneration() throws Exception {

        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName("Pica Pau");
        userCreateDTO.setEmail("jubileu" + System.currentTimeMillis() + "@gmail.com");
        userCreateDTO.setSenha("senha123");
        userCreateDTO.setRole(Role.USER);

        System.out.println("Registrando usuário: " + objectMapper.writeValueAsString(userCreateDTO));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userCreateDTO.getEmail()))
                .andExpect(jsonPath("$.name").value("Pica Pau"))
                .andExpect(jsonPath("$.role").value("USER"));


        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(userCreateDTO.getEmail());
        loginRequestDTO.setSenha("senha123");

        System.out.println("Fazendo login com: " + objectMapper.writeValueAsString(loginRequestDTO));

        String response1 = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();


        Thread.sleep(1000);


        String response2 = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();


        String token1 = objectMapper.readTree(response1).get("token").asText();
        String token2 = objectMapper.readTree(response2).get("token").asText();

        System.out.println("Token 1: " + token1);
        System.out.println("Token 2: " + token2);

        assertNotEquals(token1, token2, "Os tokens gerados devem ser diferentes");
    }
}