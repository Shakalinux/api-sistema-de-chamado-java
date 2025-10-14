package com.shakalinux.Web.model;

import com.shakalinux.Web.utils.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
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
@Entity(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idUser;

    @Column(columnDefinition = "VARCHAR(50)", nullable = false)
    private String name;

    @Email(message = "Email invalido")
    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;





    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chamado> chamados = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}