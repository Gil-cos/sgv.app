package br.com.gilberto.sgv.domain.route;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Period {

    DAYTIME("Diurno"),
    NOCTURNAL("Noturno");

    private final String prettyName;
}
