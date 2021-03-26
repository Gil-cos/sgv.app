package br.com.gilberto.sgv.domain.user;

import android.text.Editable;

import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.user.driver.DriverInfo;
import br.com.gilberto.sgv.domain.user.passenger.PassengerInfo;
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
    private Role role;
    private Address address;
    private PassengerInfo passengerInfo;
    private DriverInfo driverInfo;


    public User(final String name, final String phone, final String cpf, final String email, final String password,
                final Role role) {
        this.name = name;
        this.phone = phone;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User update(final String name, final String cpf, final String cep, final String number, final String city, final String street, final String neighborhood) {
        this.name = name;
        this.cpf = cpf;
        this.address.update(cep, number, city, street, neighborhood);
        return this;
    }
}
