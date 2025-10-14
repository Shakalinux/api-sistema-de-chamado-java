package com.shakalinux.Web.dto;

import com.shakalinux.Web.model.Resposta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespostaDTO {

    private Long idResposta;
    private String texto;

    private Long idChamado;

    private Long idUsuario;
    private String nomeUsuario;

    private LocalDateTime createdAt;

    public static RespostaDTO fromEntity(Resposta resposta) {
        RespostaDTO dto = new RespostaDTO();
        dto.setIdResposta(resposta.getIdResposta());
        dto.setTexto(resposta.getTexto());
        dto.setIdChamado(resposta.getChamado().getIdChamado());
        dto.setIdUsuario(resposta.getUsuario().getIdUser());
        dto.setNomeUsuario(resposta.getUsuario().getName());
        dto.setCreatedAt(resposta.getCreatedAt());
        return dto;
    }

}
