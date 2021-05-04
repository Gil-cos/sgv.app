package br.com.gilberto.sgv.domain.user.driver;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle  implements Serializable {

    private Long id;
    private String brand;
    private String model;
    private String licensePlate;
    private Integer numberOfSeats;

    public Vehicle(final String brand, final String model, final String licensePlate, final Integer numberOfSeats) {
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.numberOfSeats = numberOfSeats;
    }

    public void update(final Vehicle vehicle) {
        this.brand = vehicle.getBrand();
        this.model = vehicle.getModel();
        this.licensePlate = vehicle.getLicensePlate();
        this.numberOfSeats = vehicle.getNumberOfSeats();
    }
}
