package com.example.drinkapp.Database.Local;

import com.example.drinkapp.Database.DataSource.ICartDataSource;
import com.example.drinkapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource {
    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }
    public static CartDataSource getInstance(CartDAO cartDAO)
    {
        if(instance==null){
            instance=new CartDataSource(cartDAO);
        }
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItem() {
        return cartDAO.getCartItem();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return cartDAO.getCartItem();
    }

    @Override
    public int countCartItem() {
        return cartDAO.countCartItem();
    }

    @Override
    public float sumPrice() {
        return cartDAO.sumPrice();
    }

    @Override
    public void entyCart() {
        cartDAO.entyCart();

    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);

    }

    @Override
    public void updateToCart(Cart... carts) {
        cartDAO.updateToCart(carts);

    }

    @Override
    public void deleteToCart(Cart... carts) {
        cartDAO.deleteToCart(carts);

    }
}
