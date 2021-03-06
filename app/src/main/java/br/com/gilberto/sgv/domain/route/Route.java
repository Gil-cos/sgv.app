package br.com.gilberto.sgv.domain.route;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.institution.Institution;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.domain.user.driver.DriverInfo;
import br.com.gilberto.sgv.domain.user.passenger.PassengerInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route implements Serializable {

    private Long id;
    private String description;
    private Period period;
    private RouteStatus status;
    private User driver;
    private Institution institution;
    private String sharedLocationLink;


    public boolean isStandBy() {
        return RouteStatus.STAND_BY.equals(status);
    }

    public boolean isPreparing() {
        return RouteStatus.PREPARING.equals(status);
    }

    public boolean isTraveling() {
        return RouteStatus.TRAVELING.equals(status);
    }

    public boolean isActivated() {
        return isPreparing() || isTraveling();
    }

}
