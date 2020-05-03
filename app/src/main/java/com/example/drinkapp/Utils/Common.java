package com.example.drinkapp.Utils;

import com.example.drinkapp.Database.DataSource.CartRepository;
import com.example.drinkapp.Database.DataSource.FavoriteRepository;
import com.example.drinkapp.Database.Local.VinhHoangRoomDatabase;
import com.example.drinkapp.Retrofit.IDrinkShopAPI;
import com.example.drinkapp.Retrofit.RetrofitClient;
import com.example.drinkapp.Retrofit.RetrofitScalarsClient;
import com.example.drinkapp.models.Category;
import com.example.drinkapp.models.Drink;
import com.example.drinkapp.models.Order;
import com.example.drinkapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static final String BASE_URL = "http://drinkshops.000webhostapp.com/drinkshop/";
    //public static final String BASE_URL = "http://10.8.9.103/drinkshop/";
    public static final String API_TOKEN_URL = "http://drinkshops.000webhostapp.com/drinkshop/braintree/main.php";
    public static final String TOPPING_MENU_ID="12";
    public static Double toppingPrice=0.0;
    public static int sizeOfCup=-1;
    public static int sugar=-1;
    public static int ice=-1;
    public static User currentUser=null;
    public static Order currentOrder=null;
    //Database
    public static VinhHoangRoomDatabase vinhHoangRoomDatabase;
    public static CartRepository cartRepository;
    public static FavoriteRepository favoriteRepository;

    public static List<Drink> toppingList=new ArrayList<>();
    public static List<String> toppingAdded=new ArrayList<>();
    public static Category currentCategory=null;

    public static IDrinkShopAPI getAPI(){
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }
    public static IDrinkShopAPI getScalarsAPI(){
        return RetrofitScalarsClient.getScalarsClient(BASE_URL).create(IDrinkShopAPI.class);
    }

    public static String convertCodeToStatus(int orderStatus) {
        switch (orderStatus) {
            case 0:
                return "Mới nhất";
            case 1:
                return "Đang xử lý";
            case 2:
                return "Đang vận chuyển";
            case 3:
                return "Hoàn thành";
            case -1:
                return "Đã hủy";
                default:
                    return "order error";
        }
    }
}
