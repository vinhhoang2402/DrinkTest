package com.example.drinkapp.Database.DataSource;

import com.example.drinkapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource {
    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.iCartDataSource = iCartDataSource;
    }
    private static CartRepository instence;
    public static CartRepository getInstance(ICartDataSource iCartDataSource)
    {
        if (instence==null)
        {
            instence=new CartRepository(iCartDataSource);
        }
        return instence;
    }

    @Override
    public Flowable<List<Cart>> getCartItem() {
        return iCartDataSource.getCartItem();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return iCartDataSource.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItem() {
        return iCartDataSource.countCartItem();
    }

    @Override
    public float sumPrice() {
        return iCartDataSource.sumPrice();
    }

    @Override
    public void entyCart() {
        iCartDataSource.entyCart();

    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);

    }

    @Override
    public void updateToCart(Cart... carts) {
        iCartDataSource.updateToCart(carts);

    }

    @Override
    public void deleteToCart(Cart... carts) {
        iCartDataSource.deleteToCart(carts);

    }
}
