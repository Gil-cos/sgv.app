package br.com.gilberto.sgv.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private Long id;
    private String cep;
    private String street;
    private String neighborhood;
    private String city;
    private String number;
}
