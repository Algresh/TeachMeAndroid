package ru.tulupov.alex.teachme.models.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.Subject;
import ru.tulupov.alex.teachme.models.Subway;

public interface MainApi {

    @GET("/api/get/cities")
    Call<List<City>> getCities();

    @GET("/api/get/subjects")
    Call<List<Subject>> getSubjects();

    @GET("/api/get/subways")
    Call<List<Subway>> getSubways(@Query("city") int id);

}
