package ru.tulupov.alex.teachme.models;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.models.api.UserApi;
import ru.tulupov.alex.teachme.models.user.TeacherUser;
import ru.tulupov.alex.teachme.models.user.User;

public class ModelUserInfo {

    public static final int TYPE_ERROR_NOT_ENABLED = 1;
    public static final int TYPE_ERROR_PAS_LOG_ERR = 2;
    public static final int TYPE_ERROR_OTHER = -1;

    public interface ModelCallBack {
        void success(Map fields);
        void error(int type);
    }

    public interface RegTeacherCallBack {
        void success();
        void error(int type);
    }

    public void login(String login, String password, final ModelCallBack callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi api = retrofit.create(UserApi.class);
        Call<Object> call = api.login(login, password);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response == null || response.body() == null) {
                    callback.error(TYPE_ERROR_OTHER);
                    return;
                }

                String json = response.body().toString();
                Log.d(Constants.MY_TAG, json);
                Gson gson = new GsonBuilder().create();
                Map fields = gson.fromJson(json, Map.class);
                if (response.code() == 403) {
                    String warning = (String) fields.get("warning");
                    if (warning.equals("loginOrPasswordError")) {
                        callback.error(TYPE_ERROR_PAS_LOG_ERR);
                    } else if(warning.equals("notEnabled")) {
                        callback.error(TYPE_ERROR_NOT_ENABLED);
                    } else {
                        callback.error(TYPE_ERROR_OTHER);
                    }
                } else {
                    callback.success(fields);
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d(Constants.MY_TAG, "error: " + t.getMessage());
            }
        });
    }

    public void registerTeacher(final RegTeacherCallBack callBack) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TeacherRegistration teacherRegistration = TeacherRegistration.getInstance();
//        Bitmap photo = teacherRegistration.getPhoto();
        Map<String, String> map = teacherRegistration.getMapData();
        List<PriceList> priceLists = teacherRegistration.getPriceLists();
        ArrayList<String> listSbj = new ArrayList<>();
        ArrayList<String> listPrice = new ArrayList<>();
        ArrayList<String> listExp = new ArrayList<>();

        for (PriceList priceList : priceLists) {
            listSbj.add(String.valueOf(priceList.getSubject().getId()));
            listPrice.add(String.valueOf(priceList.getPrice()));
            listExp.add(priceList.getExperience());
        }

        UserApi api = retrofit.create(UserApi.class);
        Call<Object> call = api.registrationTeacher(map, listSbj, listPrice, listExp);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(Constants.MY_TAG, "reg success");
                if (response != null && response.body() != null) {
                    Log.d(Constants.MY_TAG, response.body().toString());
                }
                callBack.success();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d(Constants.MY_TAG, "reg fail " + t.getMessage());
            }
        });
    }
}
