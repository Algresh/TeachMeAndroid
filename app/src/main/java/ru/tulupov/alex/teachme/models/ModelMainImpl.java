package ru.tulupov.alex.teachme.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.models.api.MainApi;
import ru.tulupov.alex.teachme.models.api.UserApi;


public class ModelMainImpl {

    public interface ModelMainCitiesCallBack {
        void success(List<City> list);
        void error();
    }

    public interface ModelMainSubjectsCallBack {
        void success(List<Subject> list);
        void error();
    }

    public interface ModelMainSubwaysCallBack {
        void success(List<Subway> list);
        void error();
    }

    public interface ModelMainTeachersCallBack {
        void success(List<Teacher> list);
        void error();
    }

    public void getCities( final ModelMainCitiesCallBack callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainApi api = retrofit.create(MainApi.class);
        Call<List<City>> call = api.getCities();
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                callback.error();
            }
        });
    }

    public void getSubjects (final ModelMainSubjectsCallBack callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainApi api = retrofit.create(MainApi.class);
        Call<List<Subject>> call = api.getSubjects();
        call.enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                callback.error();
            }
        });
    }

    public void getSubways (int city, final ModelMainSubwaysCallBack callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainApi api = retrofit.create(MainApi.class);
        Call<List<Subway>> call = api.getSubways(city);
        call.enqueue(new Callback<List<Subway>>() {
            @Override
            public void onResponse(Call<List<Subway>> call, Response<List<Subway>> response) {
                Log.d(Constants.MY_TAG, response.body() + " id");
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<List<Subway>> call, Throwable t) {

            }
        });
    }

    public void getTeachersByCity (int city,int page, final ModelMainTeachersCallBack callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainApi api = retrofit.create(MainApi.class);
        Call<List<Teacher>> call = api.getTeachersByCity(city, page);
        call.enqueue(new Callback<List<Teacher>>() {
            @Override
            public void onResponse(Call<List<Teacher>> call, Response<List<Teacher>> response) {
                if(response == null || response.body() == null) {
                    callback.error();
                    return;
                }

                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<List<Teacher>> call, Throwable t) {
                callback.error();
            }
        });
    }
}
