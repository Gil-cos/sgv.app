package br.com.gilberto.sgv.client;

import java.util.List;

import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.dto.TokenDto;
import br.com.gilberto.sgv.wrapper.LoginWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SgvClient {

    @POST("/users/create")
    Call<User> createUser(@Body User user);

    @PUT("/users/update")
    Call<User> updateUser(@Header("Authorization") String authorization, @Body User user);

    @POST("/api/auth")
    Call<TokenDto> login(@Body LoginWrapper loginWrapper);

    @GET("/users/me")
    Call<User> getUserInfo(@Header("Authorization") String authorization);

    @POST("/routes/create")
    Call<Route> createRoute(@Header("Authorization") String authorization, @Body Route route);

    @GET("/routes/pages")
    Call<List<Route>> getRoutes(@Header("Authorization") String authorization, @Query("driver") Long driver);
}
