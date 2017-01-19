package ru.tulupov.alex.teachme.models;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
        void success(Map fields);
        void error(int type);
    }

    public interface RegTeacherPhotoCallBack {
        void success();
        void error(int type);
    }

    public interface RegTeacherConfirmCallBack {
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

    public void registerTeacher(final RegTeacherCallBack callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TeacherRegistration teacherRegistration = TeacherRegistration.getInstance();
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
                if(response == null || response.body() == null) {
                    callback.error(TYPE_ERROR_OTHER);
                    return;
                }

                if (response.code() == 403) {
                    callback.error(TYPE_ERROR_OTHER);
                } else {
                    String json = response.body().toString();
                    Log.d(Constants.MY_TAG, json);
                    Gson gson = new GsonBuilder().create();
                    Map fields = gson.fromJson(json, Map.class);
                    callback.success(fields);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d(Constants.MY_TAG, "reg fail " + t.getMessage());
            }
        });
    }

    public void setPhoto(String accessToken, String id, final RegTeacherPhotoCallBack callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        TeacherRegistration teacherRegistration = TeacherRegistration.getInstance();
        Bitmap photo = teacherRegistration.getPhoto();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), stream.toByteArray());

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", "avatar", requestFile);

        RequestBody accessTokenBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), accessToken);

        RequestBody idBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), id);

        UserApi api = retrofit.create(UserApi.class);
        Call<Object> call = api.registerPhotoTeacher(idBody, accessTokenBody, body);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(Constants.MY_TAG, "success Photo");
                if(response == null || response.body() == null) {
                    callback.error(TYPE_ERROR_OTHER);
                    return;
                }

                if (response.code() == 403) {
                    callback.error(TYPE_ERROR_OTHER);
                } else {
                    callback.success();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                callback.error(TYPE_ERROR_OTHER);
            }
        });

    }

    public void registerConfirmationTeacher(String accessToken, String code, final RegTeacherConfirmCallBack callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi api = retrofit.create(UserApi.class);
        Call<Object> call = api.registrationConfirmationTeacher(accessToken, code);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response == null || response.body() == null) {
                    callback.error(TYPE_ERROR_OTHER);
                    return;
                }

                if (response.code() == 403) {
                    callback.error(TYPE_ERROR_OTHER);
                } else {
                    callback.success();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                callback.error(TYPE_ERROR_OTHER);
            }
        });
    }


}
