package com.example.drinkapp.Database.DataSource;

import com.example.drinkapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {

    Flowable<List<Favorite>> getFavoriteItem();

    int isFavorite(int itemId);

    void insertFav(Favorite ...favorites);

    void delete(Favorite favorite);
}
