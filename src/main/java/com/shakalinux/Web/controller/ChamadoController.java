package com.shakalinux.Web.controller;

import com.shakalinux.Web.dto.ChamadoDTO;
import com.shakalinux.Web.model.Chamado;
import com.shakalinux.Web.service.ChamadoService;
import com.shakalinux.Web.utils.StatusChamado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chamados")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Chamados", description = "Gerencia os chamados do sistema")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    @Operation(summary = "Lista todos os chamados do usuário autenticado")
    @GetMapping("/meus")
    public ResponseEntity<List<ChamadoDTO>> listarMeusChamados(
            @Parameter(description = "Usuário autenticado") @AuthenticationPrincipal UserDetails userDetails) {

        List<ChamadoDTO> chamados = chamadoService.listarPorEmail(userDetails.getUsername());
        return ResponseEntity.ok(chamados);
    }

    @Operation(summary = "Lista todos os chamados (somente admins)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/todos")
    public List<ChamadoDTO> listarTodos() {
        return chamadoService.findAll()
                .stream()
                .map(ChamadoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Recupera um chamado pelo seu ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chamado encontrado"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChamadoDTO> buscarPorId(
            @Parameter(description = "ID do chamado") @PathVariable Long id) {

        return chamadoService.findById(id)
                .map(chamado -> ResponseEntity.ok(ChamadoDTO.fromEntity(chamado)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo chamado para o usuário autenticado")
    @PostMapping
    public ResponseEntity<ChamadoDTO> criar(
            @Parameter(description = "Dados do chamado") @RequestBody @Valid com.shakalinux.Web.dto.ChamadoCreateDTO dto,
            @Parameter(description = "Usuário autenticado") @AuthenticationPrincipal UserDetails userDetails) {

        Chamado chamado = chamadoService.criarChamado(dto, userDetails.getUsername());
        return ResponseEntity.ok(ChamadoDTO.fromEntity(chamado));
    }

    @Operation(summary = "Atualiza um chamado existente (somente admins)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ChamadoDTO> atualizar(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            @Parameter(description = "Dados do chamado atualizados") @RequestBody ChamadoDTO chamadoDTO) {

        return chamadoService.findById(id)
                .map(chamadoExistente -> {
                    chamadoService.updateFromDTO(chamadoExistente, chamadoDTO);
                    Chamado atualizado = chamadoService.save(chamadoExistente);
                    return ResponseEntity.ok(ChamadoDTO.fromEntity(atualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta um chamado pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do chamado") @PathVariable Long id) {

        if (chamadoService.findById(id).isPresent()) {
            chamadoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Atualiza o status de um chamado (somente admins)")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ChamadoDTO> atualizarStatus(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            @Parameter(description = "Novo status do chamado") @RequestParam StatusChamado status) {

        try {
            Chamado atualizado = chamadoService.atualizarStatus(id, status);
            return ResponseEntity.ok(ChamadoDTO.fromEntity(atualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Lista chamados filtrados por email do usuário (somente admins)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuario")
    public ResponseEntity<List<ChamadoDTO>> listarPorEmail(
            @Parameter(description = "Email do usuário") @RequestParam String email) {

        List<ChamadoDTO> chamados = chamadoService.listarPorEmail(email);
        return ResponseEntity.ok(chamados);
    }
}
