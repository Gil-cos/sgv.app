package br.com.gilberto.sgv.client;

import br.com.gilberto.sgv.domain.User;
import br.com.gilberto.sgv.dto.TokenDto;
import br.com.gilberto.sgv.wrapper.LoginWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SgvClient {

    @POST("/users/create")
    Call<User> createUser(@Body User user);

    @POST("/api/auth")
    Call<TokenDto> login(@Body LoginWrapper loginWrapper);
}
