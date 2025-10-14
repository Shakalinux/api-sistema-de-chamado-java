package com.shakalinux.Web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shakalinux.Web.dto.ChamadoCreateDTO;
import com.shakalinux.Web.dto.ChamadoDTO;
import com.shakalinux.Web.dto.UserCreateDTO;
import com.shakalinux.Web.utils.PrioridadeChamado;
import com.shakalinux.Web.utils.Role;
import com.shakalinux.Web.utils.StatusChamado;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class ChamadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private String createUser(String name, String email, String senha, Role role) throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO(name, email, senha, role);
        userCreateDTO.setName(name);
        userCreateDTO.setEmail(email);
        userCreateDTO.setSenha(senha);
        userCreateDTO.setRole(role);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk());

        return email;
    }

    private Long createChamado(String userEmail, String texto, TipoChamado tipo, PrioridadeChamado prioridade) throws Exception {
        ChamadoCreateDTO chamadoCreateDTO = new ChamadoCreateDTO();
        chamadoCreateDTO.setTexto(texto);
        chamadoCreateDTO.setTipoChamado(tipo);
        chamadoCreateDTO.setPrioridadeChamado(prioridade);

        UserDetails userDetails = User.withUsername(userEmail)
                .password("senha123")
                .roles("USER")
                .build();

        MvcResult result = mockMvc.perform(post("/api/chamados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chamadoCreateDTO))
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
    public void testCreateChamado() throws Exception {
        String userEmail = "user" + System.currentTimeMillis() + "@gmail.com";
        createUser("Pica Pau", userEmail, "senha123", Role.USER);

        ChamadoCreateDTO chamadoCreateDTO = new ChamadoCreateDTO();
        chamadoCreateDTO.setTexto("Não consigo acessar o painel");
        chamadoCreateDTO.setTipoChamado(TipoChamado.SUPORTE_TECNICO);
        chamadoCreateDTO.setPrioridadeChamado(PrioridadeChamado.ALTA);

        mockMvc.perform(post("/api/chamados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chamadoCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(getUserDetails(userEmail, Role.USER))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto").value("Não consigo acessar o painel"))
                .andExpect(jsonPath("$.tipoChamado").value("SUPORTE_TECNICO"))
                .andExpect(jsonPath("$.prioridadeChamado").value("ALTA"))
                .andExpect(jsonPath("$.statusChamado").value("ABERTO"))
                .andExpect(jsonPath("$.nomeUsuario").value("Pica Pau"));
    }


    @Test
    public void testGetChamadoById() throws Exception {
        String userEmail = "user" + System.currentTimeMillis() + "@gmail.com";
        createUser("Pica Pau", userEmail, "senha123", Role.USER);
        Long chamadoId = createChamado(userEmail, "Problema no sistema", TipoChamado.SUPORTE_TECNICO, PrioridadeChamado.ALTA);

        mockMvc.perform(get("/api/chamados/{id}", chamadoId)
                        .with(SecurityMockMvcRequestPostProcessors.user(getUserDetails(userEmail, Role.USER))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChamado").value(chamadoId))
                .andExpect(jsonPath("$.texto").value("Problema no sistema"))
                .andExpect(jsonPath("$.tipoChamado").value("SUPORTE_TECNICO"))
                .andExpect(jsonPath("$.prioridadeChamado").value("ALTA"))
                .andExpect(jsonPath("$.statusChamado").value("ABERTO"));
    }

    @Test
    public void testUpdateChamado() throws Exception {
        String adminEmail = "admin" + System.currentTimeMillis() + "@gmail.com";
        createUser("Admin", adminEmail, "admin123", Role.ADMIN);
        String userEmail = "user" + System.currentTimeMillis() + "@gmail.com";
        createUser("Pica Pau", userEmail, "senha123", Role.USER);
        Long chamadoId = createChamado(userEmail, "Problema no sistema", TipoChamado.SUPORTE_TECNICO, PrioridadeChamado.ALTA);

        ChamadoDTO updateDTO = new ChamadoDTO();
        updateDTO.setIdChamado(chamadoId);
        updateDTO.setTexto("Sistema travando");
        updateDTO.setTipoChamado(TipoChamado.MANUTENCAO);
        updateDTO.setPrioridadeChamado(PrioridadeChamado.MEDIA);
        updateDTO.setStatusChamado(StatusChamado.EM_ATENDIMENTO);

        mockMvc.perform(put("/api/chamados/{id}", chamadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(getUserDetails(adminEmail, Role.ADMIN))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto").value("Sistema travando"))
                .andExpect(jsonPath("$.tipoChamado").value("MANUTENCAO"))
                .andExpect(jsonPath("$.prioridadeChamado").value("MEDIA"))
                .andExpect(jsonPath("$.statusChamado").value("EM_ATENDIMENTO"));
    }

    @Test
    public void testDeleteChamado() throws Exception {
        String userEmail = "user" + System.currentTimeMillis() + "@gmail.com";
        createUser("Pica Pau", userEmail, "senha123", Role.USER);
        Long chamadoId = createChamado(userEmail, "Problema no sistema", TipoChamado.SUPORTE_TECNICO, PrioridadeChamado.ALTA);

        mockMvc.perform(delete("/api/chamados/{id}", chamadoId)
                        .with(SecurityMockMvcRequestPostProcessors.user(getUserDetails(userEmail, Role.USER))))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/chamados/{id}", chamadoId)
                        .with(SecurityMockMvcRequestPostProcessors.user(getUserDetails(userEmail, Role.USER))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateStatusChamado() throws Exception {
        String adminEmail = "admin" + System.currentTimeMillis() + "@gmail.com";
        createUser("Admin", adminEmail, "admin123", Role.ADMIN);
        String userEmail = "user" + System.currentTimeMillis() + "@gmail.com";
        createUser("Pica Pau", userEmail, "senha123", Role.USER);
        Long chamadoId = createChamado(userEmail, "Problema no sistema", TipoChamado.SUPORTE_TECNICO, PrioridadeChamado.ALTA);

        mockMvc.perform(patch("/api/chamados/{id}/status", chamadoId)
                        .param("status", "RESOLVIDO")
                        .with(SecurityMockMvcRequestPostProcessors.user(getUserDetails(adminEmail, Role.ADMIN))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusChamado").value("RESOLVIDO"));
    }



    @Test
    public void testListChamadosByEmail() throws Exception {
        String adminEmail = "admin" + System.currentTimeMillis() + "@gmail.com";
        createUser("Admin", adminEmail, "admin123", Role.ADMIN);
        String userEmail = "user" + System.currentTimeMillis() + "@gmail.com";
        createUser("Pica Pau", userEmail, "senha123", Role.USER);
        createChamado(userEmail, "Problema no sistema", TipoChamado.SUPORTE_TECNICO, PrioridadeChamado.ALTA);

        mockMvc.perform(get("/api/chamados/usuario")
                        .param("email", userEmail)
                        .with(SecurityMockMvcRequestPostProcessors.user(getUserDetails(adminEmail, Role.ADMIN))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("Problema no sistema"));
    }

    @Test
    public void testCreateChamadoInvalidData() throws Exception {
        String userEmail = "user" + System.currentTimeMillis() + "@gmail.com";
        createUser("Pica Pau", userEmail, "senha123", Role.USER);

        ChamadoCreateDTO chamadoCreateDTO = new ChamadoCreateDTO();
        chamadoCreateDTO.setTexto("");
        chamadoCreateDTO.setTipoChamado(TipoChamado.SUPORTE_TECNICO);
        chamadoCreateDTO.setPrioridadeChamado(PrioridadeChamado.ALTA);

        mockMvc.perform(post("/api/chamados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chamadoCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(getUserDetails(userEmail, Role.USER))))
                .andExpect(status().isBadRequest());
    }
}
