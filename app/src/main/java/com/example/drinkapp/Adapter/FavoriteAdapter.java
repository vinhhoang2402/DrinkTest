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

import com.example.drinkapp.Database.ModelDB.Favorite;
import com.example.drinkapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    Context context;
    List<Favorite> favoriteList;

    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.fav_item_layout,parent,false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Picasso.with(context)
                .load(favoriteList.get(position).link)
                .placeholder(R.drawable.bg)
                .into(holder.image_product);
        holder.txt_name.setText(favoriteList.get(position).name);
        holder.txt_price.setText(new StringBuilder("$").append(favoriteList .get(position).price));

    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

   public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name,txt_price;
        public ImageView image_product;
        public RelativeLayout view_background;
        public LinearLayout view_foreground;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_cart_product_name);
            image_product = itemView.findViewById(R.id.img_cart_product);
            txt_price = itemView.findViewById(R.id.txt_product_price);
            view_background=itemView.findViewById(R.id.view_backgroud);
            view_foreground=itemView.findViewById(R.id.view_foreground);
        }
    }
    public void removeItem(int position)
    {
        favoriteList.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Favorite item,int position)
    {
        favoriteList.add(position,item);
        notifyItemInserted(position);
    }
}
