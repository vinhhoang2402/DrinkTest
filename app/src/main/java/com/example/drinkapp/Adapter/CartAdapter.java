package com.example.drinkapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.drinkapp.Database.ModelDB.Cart;
import com.example.drinkapp.R;
import com.example.drinkapp.Utils.Common;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        Picasso.with(context)
                .load(cartList.get(position).link)
                .placeholder(R.drawable.bg)
                .into(holder.image_product);
        holder.txt_name.setText(new StringBuilder(cartList.get(position).name)
        .append("X")
        .append(cartList.get(position).amount)
        .append(cartList.get(position).size==0? "Size M":"Size L"));
        holder.txt_sugar.setText(new StringBuilder("Sugar:")
                .append(cartList.get(position).sugar).append("%").append("\n")
                .append("ICE:").append(cartList.get(position).ice)
                .append("%").toString());

        //get price
        final double priceOneCup=cartList.get(position).price/cartList.get(position).amount;
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.txt_price.setText(decimalFormat.format(cartList.get(position).price)+"đ");
        holder.btn_sl.setNumber(String.valueOf(cartList.get(position).amount));
        holder.btn_sl.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart=cartList.get(position);
                cart.amount=newValue;
                cart.price= (int) Math.round(priceOneCup*newValue);
                Common.cartRepository.updateToCart(cart);
                holder.txt_price.setText(new StringBuilder("$").append(cartList.get(position).price));
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

   public class CartViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_name, txt_price, txt_sugar, txt_ice;
        public ImageView image_product;
        public ElegantNumberButton btn_sl;
        public RelativeLayout view_background;
        public LinearLayout view_foreground;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_cart_product_name);
            txt_price = itemView.findViewById(R.id.txt_product_price);
            txt_sugar = itemView.findViewById(R.id.txt_sugar);
            txt_ice = itemView.findViewById(R.id.txt_ice);
            image_product = itemView.findViewById(R.id.img_cart_product);
            btn_sl = itemView.findViewById(R.id.btn_soluong);
            view_background=itemView.findViewById(R.id.view_backgroud);
            view_foreground=itemView.findViewById(R.id.view_foreground);
        }
    }
    public void removeItem(int position)
    {
        cartList.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Cart item,int position)
    {
        cartList.add(position,item);
        notifyItemInserted(position);
    }
}
