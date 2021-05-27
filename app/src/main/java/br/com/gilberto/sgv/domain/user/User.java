package br.com.gilberto.sgv.domain.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.user.driver.DriverInfo;
import br.com.gilberto.sgv.domain.user.driver.Vehicle;
import br.com.gilberto.sgv.domain.user.passenger.PassengerInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private Long id;
    private String name;
    private String phone;
    private String cpf;
    private String email;
    private String password;
    private String notificationToken;
    private Role role;
    private Address address;
    private PassengerInfo passengerInfo;
    private DriverInfo driverInfo;
    private List<Route> routes = new ArrayList<>();


    public User(final String name, final String phone, final String cpf, final String email, final String password,
                final String notificationToken,final Role role) {
        this.name = name;
        this.phone = phone;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.notificationToken = notificationToken;
        this.role = role;
    }

    public User update(final String cep, final String number, final String city, final String street, final String neighborhood) {
        updateAddress(new Address(cep, number, city, street, neighborhood));
        return this;
    }

    public User update(final String brand, final String model, final String licensePlate, final Integer numberOfSeats) {
        updateDriverInfo(new DriverInfo(new Vehicle(brand, model, licensePlate, numberOfSeats)));
        return this;
    }

    private void updateAddress(final Address address) {
        if (this.address != null) {
            this.address.update(address);
        } else {
            this.address = address;
        }
    }

    private void updateDriverInfo(final DriverInfo driverInfo) {
        if (this.driverInfo != null) {
            this.driverInfo.update(driverInfo);
        } else {
            this.driverInfo = driverInfo;
        }
    }

    public boolean isDriver() {
        return role.equals(Role.DRIVER);
    }

    public boolean isPassenger() {
        return role.equals(Role.PASSENGER);
    }
}
