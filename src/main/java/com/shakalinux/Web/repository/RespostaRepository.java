package com.shakalinux.Web.repository;

import com.shakalinux.Web.model.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    List<Resposta> findByChamadoIdChamado(Long idChamado);
}