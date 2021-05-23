package com.example.drinkapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.drinkapp.Database.ModelDB.Cart;
import com.example.drinkapp.Database.ModelDB.Favorite;
import com.example.drinkapp.Interface.IItemClickListenner;
import com.example.drinkapp.R;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.models.Drink;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {
    Context context;
    List<Drink> drinks;

    public DrinkAdapter(Context context, List<Drink> drinks) {
        this.context = context;
        this.drinks = drinks;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.drink_item_layout,null);
        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrinkViewHolder holder, final int position) {
        holder.txt_Price.setText(new StringBuilder("").append(drinks.get(position).Price).append("đ"));
        holder.txt_Name.setText(drinks.get(position).Name);
        holder.btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddToCartDialog(position);
            }
        });
        Picasso.with(context)
                .load(drinks.get(position).Link)
                .into(holder.image_Drink);
        holder.setiItemClickListenner(new IItemClickListenner() {
            @Override
            public void onclick(View view) {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        //Favorites system
        if(Common.favoriteRepository.isFavorite(Integer.parseInt(drinks.get(position).ID))==1)
            holder.btn_favorites.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            holder.btn_favorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        holder.btn_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.favoriteRepository.isFavorite(Integer.parseInt(drinks.get(position).ID))!=1)
                {
                    addOrRemoveFavorite(drinks.get(position),true);
                    holder.btn_favorites.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
                else
                {
                    addOrRemoveFavorite(drinks.get(position),false);
                    holder.btn_favorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }

            }
        });

    }

    private void addOrRemoveFavorite(Drink drink, boolean isAdd) {
        Favorite favorite=new Favorite();
        favorite.id=drink.ID;
        favorite.link=drink.Link;
        favorite.name=drink.Name;
        favorite.price=drink.Price;
        favorite.menuId=drink.MenuId;
        if(isAdd)
        {
            Common.favoriteRepository.insertFav(favorite);
        }
        else
        {
            Common.favoriteRepository.delete(favorite);
        }
    }


    private void showAddToCartDialog(final int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View itemView=LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart_layout,null);
        ImageView image_product=(ImageView)itemView.findViewById(R.id.img_cart_product);
        final ElegantNumberButton btn_count=(ElegantNumberButton)itemView. findViewById(R.id.txt_count);
        //Button btn_cart=(Button) itemView.findViewById(R.id.btn_add_cart) ;
        TextView txttopping=(TextView)itemView.findViewById(R.id.txt_price_topping);
        TextView txtproduct=(TextView) itemView.findViewById(R.id.txt_cart_product_name);
        RadioButton rdi_sizeM=(RadioButton)itemView. findViewById(R.id.rdi_sizeM);
        rdi_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.sizeOfCup=0;
                }
            }
        });
        RadioButton rdi_sizeL=(RadioButton)itemView. findViewById(R.id.rdi_sizeL);
        rdi_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.sizeOfCup=1;
                }
            }
        });
        RadioButton rdi_sugar100=(RadioButton)itemView. findViewById(R.id.rdi_sugar100);
        RadioButton rdi_sugar70=(RadioButton)itemView. findViewById(R.id.rdi_sugar70);
        RadioButton rdi_sugar50=(RadioButton)itemView. findViewById(R.id.rdi_sugar50);
        RadioButton rdi_sugar30=(RadioButton)itemView. findViewById(R.id.rdi_sugar30);
        RadioButton rdi_sugarfree=(RadioButton)itemView. findViewById(R.id.rdi_sugarfree);
        //
        rdi_sugar100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.sugar=100;
                }
            }
        });
        rdi_sugar70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.sugar=70;
                }
            }
        });
        rdi_sugar50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.sugar=50;
                }
            }
        });
        rdi_sugar30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.sugar=30;
                }
            }
        });
        rdi_sugarfree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.sugar=0;
                }
            }
        });
        RadioButton rdi_ice100=(RadioButton)itemView. findViewById(R.id.rdi_ice100);
        RadioButton rdi_ice70=(RadioButton) itemView.findViewById(R.id.rdi_ice70);
        RadioButton rdi_ice50=(RadioButton) itemView.findViewById(R.id.rdi_ice50);
        RadioButton rdi_ice30=(RadioButton)itemView. findViewById(R.id.rdi_ice30);
        RadioButton rdi_icefree=(RadioButton) itemView.findViewById(R.id.rdi_icefree);
        //
        rdi_ice100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.ice=100;
                }
            }
        });
        rdi_ice70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.ice=70;
                }
            }
        });
        rdi_ice50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.ice=50;
                }
            }
        });
        rdi_ice30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.ice=30;
                }
            }
        });
        rdi_icefree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.ice=0;
                }
            }
        });
        RecyclerView recycler_Topping=(RecyclerView)itemView.findViewById(R.id.recycler_topping);
        recycler_Topping.setLayoutManager(new LinearLayoutManager(context));
        recycler_Topping.setHasFixedSize(true);

        MultiChoiceAdapter adapter=new MultiChoiceAdapter(context, Common.toppingList);
        recycler_Topping.setAdapter(adapter);


        Picasso.with(context)
                .load(drinks.get(position).Link)
                .into(image_product);
        txtproduct.setText(drinks.get(position).Name);
        builder.setView(itemView);
        builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Common.sizeOfCup==-1)
                {
                    Toast.makeText(context, "Please check size", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Common.sugar==-1)
                {
                    Toast.makeText(context, "Please check sugar", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Common.ice==-1)
                {
                    Toast.makeText(context, "Plea check ice", Toast.LENGTH_SHORT).show();
                    return;
                }
                showConfirmDialog(position,btn_count.getNumber());
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showConfirmDialog(final int position, final String number) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View itemView=LayoutInflater.from(context)
                .inflate(R.layout.confirm_add_to_cart_layout,null);
        ImageView imageView_product=(ImageView) itemView.findViewById(R.id.img_cart_product);
        final TextView textView_product=(TextView) itemView.findViewById(R.id.txt_cart_product_name);
        TextView textView_price=(TextView) itemView.findViewById(R.id.txt_product_price);
        TextView textView_sugar=(TextView) itemView.findViewById(R.id.txt_sugar);
        TextView textView_ice=(TextView) itemView.findViewById(R.id.txt_ice);
        final TextView textView_topping_extra=(TextView) itemView.findViewById(R.id.txt_topping_extra);
        ///
        Picasso.with(context).load(drinks.get(position).Link).into(imageView_product);
        textView_product.setText(new StringBuilder(drinks.get(position).Name).append(" x")
                .append(Common.sizeOfCup==0?" Size M ":" Size L ")
                .append(number).toString());

        textView_ice.setText(new StringBuilder("Ice:").append(Common.ice).append("%").toString());
        textView_sugar.setText(new StringBuilder("Sugar:").append(Common.sugar).append("%").toString());
        double price=(Double.parseDouble(drinks.get(position).Price)*Double.parseDouble(number))+Common.toppingPrice;
        if(Common.sizeOfCup==1)
            price+=(3000*Double.parseDouble(number));


        StringBuilder topping_final_comment=new StringBuilder();
        for(String line:Common.toppingAdded)
            topping_final_comment.append(line).append("\n");

        textView_topping_extra.setText(topping_final_comment);
        final double finalPrice =Math.round(price);
        textView_price.setText(new StringBuilder(String.valueOf(finalPrice)).append("$"));
        builder.setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                //create new Cart Item
                Cart cartItem = new Cart();
                cartItem.name = drinks.get(position).Name;
                cartItem.amount= Integer.parseInt(number);
                cartItem.ice = Common.ice;
                cartItem.sugar = Common.sugar;
                cartItem.price = (int) finalPrice;
                cartItem.size=Common.sizeOfCup;
                cartItem.toppingExtras = textView_topping_extra.getText().toString();
                cartItem.link=drinks.get(position).Link;
                //add to db
                Common.cartRepository.insertToCart(cartItem);
                Log.d("vinhhoang", new Gson().toJson(cartItem));
                Toast.makeText(context, "save than cong", Toast.LENGTH_SHORT).show();
            }
                catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                }
        });
        builder.setView(itemView);
        builder.show();
    }
    @Override
    public int getItemCount() {
        return drinks.size();
    }
}
