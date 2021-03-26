package br.com.gilberto.sgv.domain.address;

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

    public void update(final String cep, final String number, final String city, final String street, final String neighborhood) {
        this.cep = cep;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.number = number;
    }
}
