package com.shakalinux.Web.dto;

import com.shakalinux.Web.utils.PrioridadeChamado;
import com.shakalinux.Web.utils.StatusChamado;
import com.shakalinux.Web.utils.TipoChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChamadoCreateDTO {

    @NotBlank(message = "Texto é obrigatório")
    private String texto;

    @NotNull(message = "Tipo do chamado é obrigatório")
    private TipoChamado tipoChamado;

    @NotNull(message = "Prioridade é obrigatória")
    private PrioridadeChamado prioridadeChamado;
}
