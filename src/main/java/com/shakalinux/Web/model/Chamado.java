package com.shakalinux.Web.model;

import com.shakalinux.Web.utils.PrioridadeChamado;
import com.shakalinux.Web.utils.StatusChamado;
import com.shakalinux.Web.utils.TipoChamado;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "chamado")
public class Chamado {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idChamado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoChamado tipoChamado;


    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusChamado statusChamado = StatusChamado.ABERTO;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeChamado prioridadeChamado;


    @Column(nullable = false, length = 100)
    @Size(max = 100)
    private String texto;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User usuario;


    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas = new ArrayList<>();


}