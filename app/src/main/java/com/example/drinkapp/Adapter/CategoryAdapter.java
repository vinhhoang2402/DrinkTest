package com.example.drinkapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.DrinkActivity;
import com.example.drinkapp.Interface.IItemClickListenner;
import com.example.drinkapp.R;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.models.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    Context context;
    List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.menu_item_layout,null);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        if (categories.get(position).Link != null) {
            Picasso.with(context)
                    .load(categories.get(position).Link)
                    .into(holder.imageView_Produce);
        }


        holder.txtView_Name.setText(categories.get(position).Name);
        holder.setiItemClickListenner(new IItemClickListenner() {
            @Override
            public void onclick(View view) {
                Common.currentCategory=categories.get(position);
                context.startActivity(new Intent(context, DrinkActivity.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
