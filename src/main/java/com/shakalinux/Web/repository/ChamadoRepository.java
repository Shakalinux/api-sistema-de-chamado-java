package com.shakalinux.Web.repository;

import com.shakalinux.Web.model.Chamado;
import com.shakalinux.Web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    List<Chamado> findByUsuario(User user);

}