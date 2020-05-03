package com.example.drinkapp.Retrofit;
import com.example.drinkapp.models.Banner;
import com.example.drinkapp.models.Category;
import com.example.drinkapp.models.CheckUserResponse;
import com.example.drinkapp.models.Drink;
import com.example.drinkapp.models.Order;
import com.example.drinkapp.models.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IDrinkShopAPI {
    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                                @Field("name") String name,
                                @Field("address") String address,
                                @Field("birthdate") String birthdate);

    @FormUrlEncoded
    @POST("getUser.php")
    Call<User> getUserInfomation(@Field("phone") String phone);

    @GET("get_banner.php")
    Observable<List<Banner>> getBanner();

    @GET("get_category.php")
    Observable<List<Category>> getCategory();

    @FormUrlEncoded
    @POST("get_drink_by_menu.php")
    Observable<List<Drink>> getDrink(@Field("menuid") String menuId);

    @Multipart
    @POST("updateAvatar.php")
    Call<String> uploadFile(@Part MultipartBody.Part phone, @Part MultipartBody.Part file);

    @GET("get_alldrink.php")
    Observable<List<Drink>> getAllDrinks();

    @FormUrlEncoded
    @POST("submitOrder.php")
    Call<String> submitOrder(@Field("price") float orderPrice,
                             @Field("orderDetail") String orderDetail,
                             @Field("comment") String comment,
                             @Field("address") String address,
                             @Field("phone") String phone);

    @FormUrlEncoded
    @POST("braintree/checkout.php")
    Call<String> payment(@Field("nonce") String nonce,
                             @Field("amount") String amount);


    @FormUrlEncoded
    @POST("get_order.php")
    Observable<List<Order>> getOrder(@Field("phone") String phone,
                                     @Field("status") String status );

    @FormUrlEncoded
    @POST("cancelOrder.php")
    Call<String> cancelOrder(@Field("orderId") String orderid,
                             @Field("userPhone") String userphone);
}
