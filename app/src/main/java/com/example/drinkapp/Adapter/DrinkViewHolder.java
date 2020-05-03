package com.example.drinkapp.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.Interface.IItemClickListenner;
import com.example.drinkapp.R;

public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txt_Name,txt_Price,txt_banner;
    public ImageView image_Drink;
    public ImageView btn_cart,btn_favorites;
    IItemClickListenner iItemClickListenner;

    public void setiItemClickListenner(IItemClickListenner iItemClickListenner) {
        this.iItemClickListenner = iItemClickListenner;
    }

    public DrinkViewHolder(@NonNull View itemView) {
        super(itemView);
        image_Drink = (ImageView) itemView.findViewById(R.id.Image_Product);
        txt_Name= (TextView) itemView.findViewById(R.id.txtDink_Name);
        txt_Price=(TextView) itemView.findViewById(R.id.txtDink_Price);
        txt_banner=(TextView) itemView.findViewById(R.id.txtMenu_Name);
        btn_cart=(ImageView) itemView.findViewById(R.id.btn_add_cart);
        btn_favorites=(ImageView) itemView.findViewById(R.id.btn_favorite);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iItemClickListenner.onclick(v);
    }
}
