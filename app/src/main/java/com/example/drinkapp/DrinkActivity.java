package com.example.drinkapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.Adapter.DrinkAdapter;
import com.example.drinkapp.Retrofit.IDrinkShopAPI;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.models.Drink;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DrinkActivity extends AppCompatActivity {
    IDrinkShopAPI mService;
    RecyclerView list_drink;
    TextView txt_bannerName;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        mService = Common.getAPI();
        Anhxa();
    }

    private void Anhxa() {
        txt_bannerName = (TextView) findViewById(R.id.txtMenu_Name);
        txt_bannerName.setText(Common.currentCategory.Name);
        list_drink = (RecyclerView) findViewById(R.id.recycler_Drink);
        list_drink.setLayoutManager(new GridLayoutManager(this,2));
        list_drink.setHasFixedSize(true);
        loadListDrink(Common.currentCategory.ID);
    }

    private void loadListDrink(String menuId) {
        compositeDisposable.add(mService.getDrink(menuId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        displayDrinkList(drinks);
                    }
                }));

    }

    private void displayDrinkList(List<Drink> drinks) {
        DrinkAdapter drinkAdapter=new DrinkAdapter(this,drinks);
        list_drink.setAdapter(drinkAdapter);
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
    protected void onPostResume() {
        super.onPostResume();
        isBackClick=false;
    }
}
