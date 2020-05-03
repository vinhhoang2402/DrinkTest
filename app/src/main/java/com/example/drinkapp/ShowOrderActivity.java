package com.example.drinkapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.Adapter.OrderAdapter;
import com.example.drinkapp.Retrofit.IDrinkShopAPI;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.models.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ShowOrderActivity extends AppCompatActivity {
    IDrinkShopAPI mService;
    RecyclerView recycler_order;
    BottomNavigationView bottomNavigationView;
    CompositeDisposable compositeDisposable=new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        mService= Common.getAPI();
        recycler_order=findViewById(R.id.recycler_orders);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.order_new)
                {
                    loadOrder("0");
                }
                else if(menuItem.getItemId()==R.id.order_processing)
                {
                    loadOrder("1");
                }
                else if(menuItem.getItemId()==R.id.order_cancel)
                {
                    loadOrder("-1");
                }
                else if(menuItem.getItemId()==R.id.order_shipping)
                {
                    loadOrder("2");
                }
                else if(menuItem.getItemId()==R.id.order_shipped) {
                    loadOrder("3");
                }
                return true;
            }
        });

        recycler_order.setLayoutManager(new LinearLayoutManager(this));
        recycler_order.setHasFixedSize(true);

        loadOrder("0");
    }
    private void loadOrder(String statusCode) {
        if(Common.currentUser!=null)
        {
            compositeDisposable.add(mService.getOrder(Common.currentUser.getPhone(),statusCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Order>>() {
                        @Override
                        public void accept(List<Order> orders) throws Exception {
                            displayOrder(orders);
                        }
                    }));
        }
        else
        {
            Toast.makeText(this, "Please Login Again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrder("0");
    }

    private void displayOrder(List<Order> orders) {
        OrderAdapter orderAdapter=new OrderAdapter(this,orders);
        recycler_order.setAdapter(orderAdapter);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
