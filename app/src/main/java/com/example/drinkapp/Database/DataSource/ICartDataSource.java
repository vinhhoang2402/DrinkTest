package com.example.drinkapp.Database.DataSource;

import com.example.drinkapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {
    Flowable<List<Cart>> getCartItem();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    int countCartItem();
    float sumPrice();
    void entyCart();
    void insertToCart(Cart ...carts);
    void updateToCart(Cart ...carts);
    void deleteToCart(Cart ...carts);
}
