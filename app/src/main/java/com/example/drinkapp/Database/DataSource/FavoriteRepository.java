package com.example.drinkapp.Database.DataSource;

import com.example.drinkapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteRepository implements IFavoriteDataSource {
    private  IFavoriteDataSource favoriteDataSource;

    public FavoriteRepository(IFavoriteDataSource favoriteDataSource) {
        this.favoriteDataSource = favoriteDataSource;
    }

    private static FavoriteRepository instence;
    public static FavoriteRepository getInstance(IFavoriteDataSource favoriteDataSource)
    {
        if (instence==null)
        {
            instence=new FavoriteRepository(favoriteDataSource);
        }
        return instence;
    }

    @Override
    public Flowable<List<Favorite>> getFavoriteItem() {
        return favoriteDataSource.getFavoriteItem();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void insertFav(Favorite... favorites) {
        favoriteDataSource.insertFav(favorites);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteDataSource.delete(favorite);

    }
}
