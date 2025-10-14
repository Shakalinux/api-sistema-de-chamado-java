package com.shakalinux.Web.service;

import com.shakalinux.Web.dto.ChamadoCreateDTO;
import com.shakalinux.Web.dto.ChamadoDTO;
import com.shakalinux.Web.model.Chamado;
import com.shakalinux.Web.model.User;
import com.shakalinux.Web.repository.ChamadoRepository;
import com.shakalinux.Web.repository.UserRepository;
import com.shakalinux.Web.utils.StatusChamado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;


    @Autowired
    private UserRepository userRepository;

    public List<Chamado> findAll() {
        return chamadoRepository.findAll();
    }

    public Optional<Chamado> findById(Long id) {
        return chamadoRepository.findById(id);
    }

    public Chamado save(Chamado chamado) {
        return chamadoRepository.save(chamado);
    }

    public void deleteById(Long id) {
        chamadoRepository.deleteById(id);
    }


    public Chamado fromDTO(ChamadoDTO dto) {
        Chamado chamado = new Chamado();
        chamado.setIdChamado(dto.getIdChamado());
        chamado.setTipoChamado(dto.getTipoChamado());
        chamado.setStatusChamado(dto.getStatusChamado());
        chamado.setPrioridadeChamado(dto.getPrioridadeChamado());
        chamado.setTexto(dto.getTexto());

        if (dto.getIdUsuario() != null) {
            Optional<User> userOpt = userRepository.findById(dto.getIdUsuario());
            userOpt.ifPresent(chamado::setUsuario);
        }

        return chamado;
    }


    public void updateFromDTO(Chamado chamado, ChamadoDTO dto) {
        chamado.setTipoChamado(dto.getTipoChamado());
        chamado.setStatusChamado(dto.getStatusChamado());
        chamado.setPrioridadeChamado(dto.getPrioridadeChamado());
        chamado.setTexto(dto.getTexto());

        if (dto.getIdUsuario() != null) {
            userRepository.findById(dto.getIdUsuario()).ifPresent(chamado::setUsuario);
        }
    }




    public Chamado criarChamado(ChamadoCreateDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Chamado chamado = new Chamado();
        chamado.setUsuario(user);
        chamado.setTexto(dto.getTexto());
        chamado.setTipoChamado(dto.getTipoChamado());
        chamado.setPrioridadeChamado(dto.getPrioridadeChamado());
        chamado.setStatusChamado(StatusChamado.ABERTO);
        chamado.setUsuario(user);

        return chamadoRepository.save(chamado);
    }

    public List<ChamadoDTO> listarPorEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return chamadoRepository.findByUsuario(user)
                .stream()
                .map(ChamadoDTO::fromEntity)
                .collect(Collectors.toList());
    }


    @Transactional


    public Chamado atualizarStatus(Long id, StatusChamado status) {
        Chamado chamado = findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));
        chamado.setStatusChamado(status);
        return save(chamado);
    }
}
