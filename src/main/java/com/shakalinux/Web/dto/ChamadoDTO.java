package com.shakalinux.Web.dto;

import com.shakalinux.Web.model.Chamado;
import com.shakalinux.Web.utils.PrioridadeChamado;
import com.shakalinux.Web.utils.StatusChamado;
import com.shakalinux.Web.utils.TipoChamado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoDTO {

    private Long idChamado;
    private TipoChamado tipoChamado;
    private StatusChamado statusChamado;
    private PrioridadeChamado prioridadeChamado;
    private String texto;

    private Long idUsuario;
    private String nomeUsuario;


    public static ChamadoDTO fromEntity(Chamado chamado) {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setIdChamado(chamado.getIdChamado());
        dto.setTipoChamado(chamado.getTipoChamado());
        dto.setStatusChamado(chamado.getStatusChamado());
        dto.setPrioridadeChamado(chamado.getPrioridadeChamado());
        dto.setTexto(chamado.getTexto());

        if (chamado.getUsuario() != null) {
            dto.setIdUsuario(chamado.getUsuario().getIdUser());
            dto.setNomeUsuario(chamado.getUsuario().getName());
        }

        return dto;
    }
}
