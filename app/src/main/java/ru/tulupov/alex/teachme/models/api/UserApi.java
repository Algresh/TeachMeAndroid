package ru.tulupov.alex.teachme.models.api;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @GET("/api/login/user")
    Call<Object> login(@Query("login") String login, @Query("password") String password);

    @FormUrlEncoded
    @POST("/api/register/teacher")
    Call<Object> registrationTeacher(
            @FieldMap Map<String, String> map,
            @Field("priceLIstSbj[]") ArrayList<String> priceLIstSbj,
            @Field("priceLIstPrice[]") ArrayList<String> priceLIstPrice,
            @Field("priceLIstExp[]") ArrayList<String> priceLIstExp
    );
}
