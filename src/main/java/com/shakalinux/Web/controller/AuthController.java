package com.shakalinux.Web.controller;

import com.shakalinux.Web.dto.LoginRequestDTO;
import com.shakalinux.Web.dto.LoginResponseDTO;
import com.shakalinux.Web.model.User;
import com.shakalinux.Web.repository.UserRepository;
import com.shakalinux.Web.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Gerencia login de usuários e geração de JWT")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário e retorna um token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso, token gerado"),
            @ApiResponse(responseCode = "401", description = "Email ou senha inválidos")
    })
    public ResponseEntity<?> login(
            @Parameter(description = "Objeto contendo email e senha do usuário") @RequestBody LoginRequestDTO dto) {

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));

            User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
        }
    }
}
