package com.example.drinkapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.Interface.IItemClickListenner;
import com.example.drinkapp.OrderDetailActivity;
import com.example.drinkapp.R;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    Context context;
    List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.order_layout,parent,false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        holder.txt_order_id.setText(new StringBuilder("Mã đơn hàng: ").append(orderList.get(position).getOderId()));
        holder.txt_order_price.setText(new StringBuilder("Tổng tiền: ").append(orderList.get(position).getOrderPrice()));
        holder.txt_order_date.setText(new StringBuilder("Ngày Order: ").append(orderList.get(position).getOrderDate()));
        holder.txt_order_address.setText(new StringBuilder("Địa chỉ nhận hàng: ").append(orderList.get(position).getOrderAddress()));
        holder.txt_order_status.setText(new StringBuilder("Tình trạng đơn hàng: ").append(Common.convertCodeToStatus(orderList.get(position).getOrderStatus())));
        holder.setiItemClickListenner(new IItemClickListenner() {
            @Override
            public void onclick(View view) {
                Common.currentOrder=orderList.get(position);
                context.startActivity(new Intent(context, OrderDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
