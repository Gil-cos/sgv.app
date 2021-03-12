package br.com.gilberto.sgv.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private String phone;
    private String cpf;
    private String email;
    private String password;
    private Address address;
}
