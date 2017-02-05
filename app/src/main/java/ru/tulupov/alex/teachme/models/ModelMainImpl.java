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

    public interface ModelFavoriteCallBack {
        void success(int code);
        void error();
    }

    public interface FreezeCallBack {
        void freezeSuccess();
        void unfreezeSuccess();
        void error();
    }

    private MainApi mainApi;

    public ModelMainImpl() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mainApi = retrofit.create(MainApi.class);
    }

    public void freezeTeacher(String accessToekn, final FreezeCallBack callback) {
        Call<Object> call = mainApi.freezeTeacher(accessToekn);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response == null || response.body() == null) {
                    callback.error();
                    return;
                }

                if (response.code() == 403) {
                    callback.error();
                } else {
                    String json = response.body().toString();
                    Log.d(Constants.MY_TAG, json);
                    Gson gson = new GsonBuilder().create();
                    Map fields = gson.fromJson(json, Map.class);
                    Double enable = (Double) fields.get("enable");
                    if (enable.intValue() == 1) {
                        callback.unfreezeSuccess();
                    } else {
                        callback.freezeSuccess();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                callback.error();
            }
        });
    }

    public void getCities(final ModelMainCitiesCallBack callback) {
        Call<List<City>> call = mainApi.getCities();
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
        Call<List<Subject>> call = mainApi.getSubjects();
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
        Call<List<Subway>> call = mainApi.getSubways(city);
        call.enqueue(new Callback<List<Subway>>() {
            @Override
            public void onResponse(Call<List<Subway>> call, Response<List<Subway>> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<List<Subway>> call, Throwable t) {
                callback.error();
            }
        });
    }

    public void getTeachersByCity (int city,int page, final ModelMainTeachersCallBack callback) {
        Call<List<Teacher>> call = mainApi.getTeachersByCity(city, page);
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



    public void getTeachersSearchQuick (int city, boolean leaveHouse, int subject,
                                        int page, final ModelMainTeachersCallBack callback) {
        int leave = 0;
        if (leaveHouse) leave = 1;

        Call<List<Teacher>> call = mainApi.getTeachersQuickSearch(city, leave, subject, page);
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

    public void getTeachersSearchFull (Map<String, String> map, final ModelMainTeachersCallBack callback) {


        Call<List<Teacher>> call = mainApi.getTeachersMainSearch(map);
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

    public void setFavorite (String accessToken, int id, final ModelFavoriteCallBack callback) {
        Call<Object> call = mainApi.setFavorite(accessToken, id);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response == null || response.body() == null) {
                    callback.error();
                    return;
                }

                if (response.code() >= 200 && response.code() < 300) {
                    callback.success(response.code());
                } else {
                    callback.error();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                callback.error();
            }
        });
    }
}
