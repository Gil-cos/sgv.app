package br.com.gilberto.sgv.client;

import java.util.List;

import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.route.RouteStatus;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.dto.TokenDto;
import br.com.gilberto.sgv.wrapper.LoginWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SgvClient {

    @POST("/api/auth")
    Call<TokenDto> login(@Body LoginWrapper loginWrapper);

    @POST("/users/create")
    Call<User> createUser(@Body User user);

    @PUT("/users/update")
    Call<User> updateUser(@Header("Authorization") String authorization, @Body User user);

    @GET("/users/me")
    Call<User> getUserInfo(@Header("Authorization") String authorization);

    @GET("/users/pages")
    Call<List<User>> getPassengersByFilter(@Header("Authorization") String authorization, @Query("name") String name);

    @POST("/routes/create")
    Call<Route> createRoute(@Header("Authorization") String authorization, @Body Route route);

    @GET("/routes/{userId}/user")
    Call<List<Route>> getRoutes(@Header("Authorization") String authorization, @Path("userId") Long userId);

    @GET("/routes/{id}")
    Call<Route> getRoute(@Header("Authorization") String authorization, @Path("id") Long id);

    @GET("/routes/passengers/{id}")
    Call<List<User>> getPassengers(@Header("Authorization") String authorization, @Path("id") Long id);

    @PUT("/routes/{routeId}/passengers/add/{passengerId}")
    Call<Route> addPassenger(@Header("Authorization") String authorization, @Path("routeId") Long routeId, @Path("passengerId") Long passengerId);

    @PUT("/routes/{routeId}/passengers/remove/{passengerId}")
    Call<Route> removePassenger(@Header("Authorization") String authorization, @Path("routeId") Long routeId, @Path("passengerId") Long passengerId);

    @PUT("/routes/{id}/status/{status}")
    Call<Route> changeStatus(@Header("Authorization") String authorization, @Path("id") Long id, @Path("status") RouteStatus status);

}
