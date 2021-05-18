package br.com.gilberto.sgv.domain.institution;

import java.io.Serializable;

import br.com.gilberto.sgv.domain.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institution implements Serializable {

    private Long id;
    private String name;
    private Address address;
}
