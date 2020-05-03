package com.example.drinkapp.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.Interface.IItemClickListenner;
import com.example.drinkapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView imageView_Produce;
    public TextView txtView_Name;
    IItemClickListenner iItemClickListenner;

    public void setiItemClickListenner(IItemClickListenner iItemClickListenner) {
        this.iItemClickListenner = iItemClickListenner;
    }

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView_Produce = (ImageView)itemView.findViewById(R.id.ImageView_Produce);
        txtView_Name = (TextView)itemView.findViewById(R.id.txt_Name);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        iItemClickListenner.onclick(v);
    }
}
