package br.com.gilberto.sgv.domain.user.passenger;

import java.util.HashSet;
import java.util.Set;

import br.com.gilberto.sgv.domain.institution.Institution;
import br.com.gilberto.sgv.domain.route.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerInfo {

    private Long id;
    private Institution institution;
    private final Set<Route> routes = new HashSet<>();
}
