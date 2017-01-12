package ru.tulupov.alex.teachme.models.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @GET("/api/login/user")
    Call<Object> login(@Query("login") String login, @Query("password") String password);
}
