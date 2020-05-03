package com.example.drinkapp.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.drinkapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItem();

    @Query("SELECT * FROM Cart where id=:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT Count(*) FROM Cart")
    int countCartItem();

    @Query("SELECT SUM(price) FROM Cart")
    float sumPrice();

    @Query("DELETE FROM Cart")
    void entyCart();

    @Insert
    void insertToCart(Cart ...carts);
    @Update
    void updateToCart(Cart ...carts);
    @Delete
    void deleteToCart(Cart ...carts);


}
