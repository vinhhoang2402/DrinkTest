package com.example.drinkapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.Adapter.FavoriteAdapter;
import com.example.drinkapp.Database.ModelDB.Favorite;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.Utils.RecyclerItemTouchHelperListener;
import com.example.drinkapp.Utils.RecycleritemTouchHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    RecyclerView recyclerView_favorite;
    FavoriteAdapter favoriteAdapter;
    RelativeLayout rootLayout;
    CompositeDisposable compositeDisposable ;
    List<Favorite> localFavorites=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        compositeDisposable=new CompositeDisposable();
        recyclerView_favorite=(RecyclerView)findViewById(R.id.rc_favorite);
        recyclerView_favorite.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_favorite.setHasFixedSize(true);
        rootLayout=findViewById(R.id.rootLayout);
        ItemTouchHelper.SimpleCallback simpleCallback=new RecycleritemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView_favorite);
        loadFavoritesItem();
    }

    private void loadFavoritesItem() {
        compositeDisposable.add(Common.favoriteRepository.getFavoriteItem()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<List<Favorite>>() {
            @Override
            public void accept(List<Favorite> favorites) throws Exception {
                displayFavoriteItem(favorites);
                Log.d("tag", "fav: "+favorites);
            }
        }));
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }



    private void displayFavoriteItem(List<Favorite> favorites) {
        localFavorites=favorites;
        favoriteAdapter=new FavoriteAdapter(this,favorites);
        recyclerView_favorite.setAdapter(favoriteAdapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof FavoriteAdapter.FavoriteViewHolder)
        {
            String name=localFavorites.get(viewHolder.getAdapterPosition()).name;
            final Favorite deleteItem=localFavorites.get(viewHolder.getAdapterPosition());
            final  int deletedIndex=viewHolder.getAdapterPosition();
            favoriteAdapter.removeItem(deletedIndex);
            Common.favoriteRepository.delete(deleteItem);
            Snackbar snackbar=Snackbar.make(rootLayout,new StringBuilder(name).append("Xoa san pham nay?"),5000);
            snackbar.setAction("khong", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favoriteAdapter.restoreItem(deleteItem,deletedIndex);
                    Common.favoriteRepository.insertFav(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    boolean isBackClick = false;
    @Override
    public void onBackPressed() {
        if(isBackClick) {
            super.onBackPressed();
            return;
        }
        this.isBackClick=true;
        Toast.makeText(this, "Click BACK neu muon EXIT", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoritesItem();
        isBackClick=false;
    }



}
