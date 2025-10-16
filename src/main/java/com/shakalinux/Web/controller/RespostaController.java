package com.shakalinux.Web.controller;

import com.shakalinux.Web.dto.ErrorResponse;
import com.shakalinux.Web.dto.RespostaCreateDTO;
import com.shakalinux.Web.dto.RespostaDTO;
import com.shakalinux.Web.model.Resposta;
import com.shakalinux.Web.service.ChamadoService;
import com.shakalinux.Web.service.RespostaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/respostas")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Respostas", description = "Gerencia as respostas dos chamados")
public class RespostaController {

    @Autowired
    private RespostaService respostaService;

    @Autowired
    private ChamadoService chamadoService;

    @Operation(
            summary = "Lista respostas de um chamado pelo seu Id",
            description = "Recupera todas as respostas de um chamado específico. Somente o dono do chamado ou administradores podem acessar.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de respostas retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RespostaDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário não autorizado a acessar essas respostas",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Chamado não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/chamado/{idChamado}")
    public ResponseEntity<?> listarPorChamado(
            @Parameter(description = "ID do chamado") @PathVariable Long idChamado,
            @AuthenticationPrincipal UserDetails userDetails) {

        return chamadoService.findById(idChamado)
                .map(chamado -> {
                    boolean podeAcessar = chamado.getUsuario().getEmail().equals(userDetails.getUsername())
                            || userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                    if (!podeAcessar) {
                        return ResponseEntity.status(403)
                                .body(new ErrorResponse(403, "Acesso negado"));
                    }

                    List<RespostaDTO> respostas = respostaService.listarPorChamado(idChamado);
                    return ResponseEntity.ok(respostas);
                }).orElseGet(() ->
                        ResponseEntity.status(404).body(new ErrorResponse(404, "Chamado não encontrado"))
                );
    }

    @Operation(
            summary = "Cria uma resposta para um chamado",
            description = "Cria uma resposta vinculada a um chamado. Somente administradores podem realizar esta ação.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resposta criada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RespostaDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário não autorizado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos para criar a resposta",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criar(
            @Parameter(description = "Dados da resposta a ser criada") @RequestBody RespostaCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            Resposta resposta = respostaService.criarResposta(dto, userDetails.getUsername());
            return ResponseEntity.ok(RespostaDTO.fromEntity(resposta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        }
    }
}
