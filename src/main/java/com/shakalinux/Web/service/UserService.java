package com.shakalinux.Web.service;

import com.shakalinux.Web.dto.UserCreateDTO;
import com.shakalinux.Web.model.User;
import com.shakalinux.Web.repository.UserRepository;
import com.shakalinux.Web.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        return userRepository.save(user);
    }



    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void deleteById(Long id) {
        userRepository.deleteById(id);


    }

    public User fromDTO(UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha());
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.USER);
        return user;
    }

}
