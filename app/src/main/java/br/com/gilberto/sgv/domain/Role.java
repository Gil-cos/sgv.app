package br.com.gilberto.sgv.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("ADMIN"),
    DRIVER("DRIVER"),
    PASSANGER("PASSANGER"),
    SYSTEM("SYSTEM");

    private final String role;
}
