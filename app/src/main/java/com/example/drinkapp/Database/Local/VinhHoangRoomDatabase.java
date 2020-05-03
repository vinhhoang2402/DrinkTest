package com.example.drinkapp.Database.Local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.drinkapp.Database.ModelDB.Cart;
import com.example.drinkapp.Database.ModelDB.Favorite;

@Database(entities = { Cart.class, Favorite.class}, version = 2,exportSchema = false)

public abstract class VinhHoangRoomDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();
    private static VinhHoangRoomDatabase instance;

    public static VinhHoangRoomDatabase getInstance( Context context) {
        if (instance == null) {
            instance= Room.databaseBuilder(context,VinhHoangRoomDatabase.class,"VinhHoangDrinkdShopDB")
                    .allowMainThreadQueries()
                    .build();
            }
        return instance;
    }
}
