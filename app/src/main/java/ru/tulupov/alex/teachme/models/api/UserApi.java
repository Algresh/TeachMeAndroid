package ru.tulupov.alex.teachme.models.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.tulupov.alex.teachme.models.ContactsBlock;
import ru.tulupov.alex.teachme.models.Description;
import ru.tulupov.alex.teachme.models.FullNameBlock;
import ru.tulupov.alex.teachme.models.PriceList;

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

    @FormUrlEncoded
    @POST("/api/register/pupil")
    Call<Object> registrationPupil(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/api/forgot/password")
    Call<Object> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("/api/forgot/password/confirmation")
    Call<Object> forgotPasswordConfirmation(
            @Field("email") String email,
            @Field("typeUser") String typeUser,
            @Field("newPassword") String newPassword,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("/api/register/confirmation/teacher")
    Call<Object> registrationConfirmationTeacher(
            @Field("accessToken") String accessToken,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("/api/register/confirmation/pupil")
    Call<Object> registrationConfirmationPupil(
            @Field("accessToken") String accessToken,
            @Field("code") String code
    );

    @Multipart
    @POST("/api/set/photo/teacher")
    Call<Object> registerPhotoTeacher(
            @Part("id") RequestBody id,
            @Part("accessToken") RequestBody accessToken,
            @Part MultipartBody.Part file
    );

    @GET("/api/check/login/email")
    Call<Object> checkEmailAndLogin(
            @Query("login") String login,
            @Query("email") String email,
            @Query("numberPhone") String phone
    );

    @GET("/api/get/teacher/fullname")
    Call<FullNameBlock> getTeacherFullName(@Query("accessToken") String accessToken);

    @GET("/api/get/teacher/description")
    Call<Description> getTeacherDescription(@Query("accessToken") String accessToken);

    @GET("/api/get/teacher/pricelist")
    Call<List<PriceList>> getTeacherPriceList(@Query("accessToken") String accessToken);

    @GET("/api/get/teacher/contacts")
    Call<ContactsBlock> getTeacherContacts(@Query("accessToken") String accessToken);


    @FormUrlEncoded
    @POST("/api/change/teacher/fullname")
    Call<FullNameBlock> changeTeacherFullName(
            @Field("accessToken") String accessToken,
            @FieldMap Map<String, String> map
    );

    @FormUrlEncoded
    @POST("/api/change/teacher/description")
    Call<Object> changeTeacherDescriptione(
            @Field("accessToken") String accessToken,
            @Field("description") String description
    );

    @FormUrlEncoded
    @POST("/api/change/teacher/subjects")
    Call<Object> changeTeacherSubjects(
            @Field("accessToken") String accessToken,
            @Field("priceLIstNum") int priceLIstNum,
            @Field("priceLIstSbj[]") ArrayList<String> priceLIstSbj,
            @Field("priceLIstPrice[]") ArrayList<String> priceLIstPrice,
            @Field("priceLIstExp[]") ArrayList<String> priceLIstExp
    );

    @FormUrlEncoded
    @POST("/api/change/teacher/contacts")
    Call<ContactsBlock> changeTeacherContacts(
            @Field("accessToken") String accessToken,
            @FieldMap Map<String, String> map
    );
}
