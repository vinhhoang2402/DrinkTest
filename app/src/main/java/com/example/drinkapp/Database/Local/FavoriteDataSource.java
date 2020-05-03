package com.example.drinkapp.Database.Local;

import com.example.drinkapp.Database.DataSource.IFavoriteDataSource;
import com.example.drinkapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteDataSource implements IFavoriteDataSource {
    private FavoriteDAO favoriteDAO;
    private static FavoriteDataSource instance;

    public static FavoriteDataSource getInstance(FavoriteDAO favoriteDAO)
    {
        if(instance==null){
            instance=new FavoriteDataSource(favoriteDAO);
        }
        return instance;
    }
    public FavoriteDataSource(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    @Override
    public Flowable<List<Favorite>> getFavoriteItem() {
        return favoriteDAO.getFavoriteItem();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDAO.isFavorite(itemId);
    }

    @Override
    public void insertFav(Favorite... favorites) {
        favoriteDAO.insertFav(favorites);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteDAO.delete(favorite);

    }
}
