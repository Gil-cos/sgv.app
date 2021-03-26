package br.com.gilberto.sgv.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("ADMIN"),
    DRIVER("DRIVER"),
    PASSENGER("PASSENGER"),
    SYSTEM("SYSTEM");

    private final String role;
}
