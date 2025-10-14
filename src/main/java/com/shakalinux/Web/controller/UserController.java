package com.shakalinux.Web.controller;

import com.shakalinux.Web.dto.UserCreateDTO;
import com.shakalinux.Web.dto.UserDTO;
import com.shakalinux.Web.model.User;
import com.shakalinux.Web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Gerencia usuários do sistema")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Registra um novo usuário no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @Parameter(description = "Dados do usuário a serem cadastrados")
            @Valid @RequestBody UserCreateDTO dto) {

        User user = userService.fromDTO(dto);
        User savedUser = userService.save(user);
        return ResponseEntity.ok(UserDTO.fromEntity(savedUser));
    }

    @Operation(summary = "Retorna os dados do usuário atualmente autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado e retornado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(
            @Parameter(description = "Usuário autenticado")
            @AuthenticationPrincipal UserDetails userDetails) {

        return userService.findByEmail(userDetails.getUsername())
                .map(user -> ResponseEntity.ok(UserDTO.fromEntity(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}
