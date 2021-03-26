package br.com.gilberto.sgv.client;

import br.com.gilberto.sgv.dto.CepDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepClient {

    @GET("{cep}/json/")
    Call<CepDto> getAddressInfoByCep(@Path("cep") String cep);
}
