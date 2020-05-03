package com.example.drinkapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.Adapter.OrderDetailAdapter;
import com.example.drinkapp.Database.ModelDB.Cart;
import com.example.drinkapp.Retrofit.IDrinkShopAPI;
import com.example.drinkapp.Utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    TextView txt_order_id,txt_order_price,txt_order_address,txt_order_status,txt_order_date;
    RecyclerView recyclerView_order_detail;
    Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);


        txt_order_price=findViewById(R.id.txt_order_prices);
        txt_order_id=findViewById(R.id.txt_order_id);
        txt_order_address=findViewById(R.id.txt_order_address);
        txt_order_status=findViewById(R.id.txt_order_status);
        txt_order_date=findViewById(R.id.txt_order_date);
        btn_cancel=findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });


        txt_order_id.setText(new StringBuilder("Mã đơn hàng ").append(Common.currentOrder.getOderId()));
        txt_order_price.setText(new StringBuilder("Tổng tiền: ").append(Common.currentOrder.getOrderPrice()));
        txt_order_date.setText(new StringBuilder("Ngày Order: ").append(Common.currentOrder.getOrderDate()));
        txt_order_address.setText(new StringBuilder("Địa chị nhận hàng: ").append(Common.currentOrder.getOrderAddress()));
        txt_order_status.setText(new StringBuilder("OrderStatus: ").append(Common.convertCodeToStatus(Common.currentOrder.getOrderStatus())));

        recyclerView_order_detail=findViewById(R.id.recyclerview_order_detail);
        recyclerView_order_detail.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_order_detail.setHasFixedSize(true);

        displayOrderDetail();

    }

    private void cancelOrder() {
        IDrinkShopAPI drinkShopAPI=Common.getAPI();
        drinkShopAPI.cancelOrder(String.valueOf(Common.currentOrder.getOderId()),
                Common.currentUser.getPhone())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(OrderDetailActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                        if(response.body().contains("Don hang da bi huy"))
                        finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("DEBUG",t.getMessage());
                    }
                });
    }

    private void displayOrderDetail() {
        List<Cart> orderDetail = new Gson().fromJson(Common.currentOrder.getOrderDetail(),
                new TypeToken<List<Cart>>(){}.getType());
        recyclerView_order_detail.setAdapter(new OrderDetailAdapter(this,orderDetail));
    }
}
