package com.shakalinux.Web.service;

import com.shakalinux.Web.dto.RespostaCreateDTO;
import com.shakalinux.Web.dto.RespostaDTO;
import com.shakalinux.Web.model.Chamado;
import com.shakalinux.Web.model.Resposta;
import com.shakalinux.Web.model.User;
import com.shakalinux.Web.repository.ChamadoRepository;
import com.shakalinux.Web.repository.RespostaRepository;
import com.shakalinux.Web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RespostaService {
    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Transactional
    public Resposta criarResposta(RespostaCreateDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Chamado chamado = chamadoRepository.findById(dto.getIdChamado())
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        Resposta resposta = new Resposta();
        resposta.setTexto(dto.getTexto());
        resposta.setUsuario(user);
        resposta.setChamado(chamado);

        return respostaRepository.save(resposta);
    }

    public List<RespostaDTO> listarPorChamado(Long idChamado) {
        return respostaRepository.findByChamadoIdChamado(idChamado)
                .stream()
                .map(RespostaDTO::fromEntity)
                .collect(Collectors.toList());
    }


}