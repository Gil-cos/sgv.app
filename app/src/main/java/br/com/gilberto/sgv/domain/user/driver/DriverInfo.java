package br.com.gilberto.sgv.domain.user.driver;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.com.gilberto.sgv.domain.route.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DriverInfo implements Serializable {

    private Long id;
    private Vehicle vehicle;

    public DriverInfo(final Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void update(final DriverInfo driverInfo) {
        this.vehicle.update(driverInfo.getVehicle());
    }
}
