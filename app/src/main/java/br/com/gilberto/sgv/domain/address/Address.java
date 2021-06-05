package br.com.gilberto.sgv.domain.address;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address implements Serializable {

    private Long id;
    private String cep;
    private String street;
    private String neighborhood;
    private String city;
    private String number;

    public Address(final String cep, final String number, final String city, final String street, final String neighborhood) {
        this.cep = cep;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.number = number;
    }

    public void update(final Address address) {
        this.cep = address.getCep();
        this.street = address.getStreet();
        this.neighborhood = address.getNeighborhood();
        this.city = address.getCity();
        this.number = address.getNumber();
    }

    public String getFormattedAddress() {
        return String.format("%s %s %s",
                this.getStreet(),
                this.getNeighborhood(),
                this.getNumber());
    }
}
