package br.com.gilberto.sgv.client;

import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.dto.TokenDto;
import br.com.gilberto.sgv.wrapper.LoginWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface SgvClient {

    @POST("/users/create")
    Call<User> createUser(@Body User user);

    @PUT("/users/update")
    Call<User> updateUser(@Header("Authorization") String authorization, @Body User user);

    @POST("/api/auth")
    Call<TokenDto> login(@Body LoginWrapper loginWrapper);

    @GET("/users/me")
    Call<User> getUserInfo(@Header("Authorization") String authorization);
}
