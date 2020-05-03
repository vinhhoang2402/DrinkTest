package com.example.drinkapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.Adapter.DrinkAdapter;
import com.example.drinkapp.Retrofit.IDrinkShopAPI;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.models.Drink;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    List<String> suggestList=new ArrayList<>();
    List<Drink> localDataSource=new ArrayList<>();
    MaterialSearchBar searchBar;
    IDrinkShopAPI mService;
    RecyclerView recyclerView_search;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    DrinkAdapter searchAdapter,adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //service
        mService= Common.getAPI();
        recyclerView_search=findViewById(R.id.recycler_search);
        recyclerView_search.setLayoutManager(new GridLayoutManager(this,2));
        searchBar=findViewById(R.id.search_bar);
        loadAllDrinks();
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest=new ArrayList<>();
                for (String search:suggestList)
                {
                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerView_search.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        List<Drink> result=new ArrayList<>();
        for(Drink drink:localDataSource)
            if(drink.Name.contains(text))
                result.add(drink);
            searchAdapter=new DrinkAdapter(this,result);
            recyclerView_search.setAdapter(searchAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    private void loadAllDrinks() {
        compositeDisposable.add(
                mService.getAllDrinks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Drink>>() {
                            @Override
                            public void accept(List<Drink> drinks) throws Exception {
                                displayListDrink(drinks);
                                buildSuggestList(drinks);
                            }
                        })
        );
    }

    private void buildSuggestList(List<Drink> drinks) {
        for (Drink drink:drinks)
            suggestList.add(drink.Name);
        searchBar.setLastSuggestions(suggestList);
    }

    private void displayListDrink(List<Drink> drinks) {
        localDataSource= drinks;
        adapter=new DrinkAdapter(this,drinks);
        recyclerView_search.setAdapter(adapter);
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
        isBackClick=false;
    }
}
