package com.shakalinux.Web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shakalinux.Web.dto.RespostaCreateDTO;
import com.shakalinux.Web.utils.Role;
import com.shakalinux.Web.dto.ChamadoCreateDTO;
import com.shakalinux.Web.utils.PrioridadeChamado;
import com.shakalinux.Web.utils.TipoChamado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
public class RespostaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private String createUser(String name, String email, String senha, Role role) throws Exception {
        var userCreateDTO = new com.shakalinux.Web.dto.UserCreateDTO(name, email, senha, role);
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk());
        return email;
    }

    private Long createChamado(String userEmail, String texto) throws Exception {
        ChamadoCreateDTO chamadoDTO = new ChamadoCreateDTO();
        chamadoDTO.setTexto(texto);
        chamadoDTO.setTipoChamado(TipoChamado.SUPORTE_TECNICO);
        chamadoDTO.setPrioridadeChamado(PrioridadeChamado.MEDIA);

        UserDetails userDetails = User.withUsername(userEmail)
                .password("senha123")
                .roles("USER")
                .build();

        MvcResult result = mockMvc.perform(post("/api/chamados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chamadoDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("idChamado").asLong();
    }

    private UserDetails getUserDetails(String email, Role role) {
        return User.withUsername(email)
                .password("senha123")
                .roles(role.name())
                .build();
    }


    @Test
    public void testCreateRespostaAsAdmin() throws Exception {
        String adminEmail = createUser("Admin", "admin@test.com", "senha123", Role.ADMIN);
        String userEmail = createUser("User", "user@test.com", "senha123", Role.USER);
        Long chamadoId = createChamado(userEmail, "Meu chamado");

        RespostaCreateDTO respostaDTO = new RespostaCreateDTO("Minha resposta", chamadoId);

        mockMvc.perform(post("/api/respostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(respostaDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                User.withUsername(adminEmail).password("senha123").roles("ADMIN").build()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto").value("Minha resposta"))
                .andExpect(jsonPath("$.idChamado").value(chamadoId));
    }

    @Test
    public void testListRespostasByChamado() throws Exception {
        String adminEmail = createUser("Admin", "admin@test.com", "senha123", Role.ADMIN);
        String userEmail = createUser("User", "user@test.com", "senha123", Role.USER);
        Long chamadoId = createChamado(userEmail, "Meu chamado");


        RespostaCreateDTO respostaDTO = new RespostaCreateDTO("Minha resposta", chamadoId);
        mockMvc.perform(post("/api/respostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(respostaDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                User.withUsername(adminEmail).password("senha123").roles("ADMIN").build()
                        )))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/respostas/chamado/{id}", chamadoId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                User.withUsername(userEmail).password("senha123").roles("USER").build()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("Minha resposta"));
    }
}
