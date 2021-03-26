package br.com.gilberto.sgv.domain.institution;

import br.com.gilberto.sgv.domain.address.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Institution {

    private Long id;
    private String name;
    private Address address;
}
