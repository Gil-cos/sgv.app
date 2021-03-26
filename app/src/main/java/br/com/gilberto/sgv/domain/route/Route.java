package br.com.gilberto.sgv.domain.route;

import java.util.HashSet;
import java.util.Set;

import br.com.gilberto.sgv.domain.institution.Institution;
import br.com.gilberto.sgv.domain.user.driver.DriverInfo;
import br.com.gilberto.sgv.domain.user.passenger.PassengerInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    private Long id;
    private String description;
    private Period period;
    private DriverInfo driver;
    private final Set<PassengerInfo> passengers = new HashSet<>();
    private Institution institution;


}
