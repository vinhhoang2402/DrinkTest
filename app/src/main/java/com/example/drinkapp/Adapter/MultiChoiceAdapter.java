package com.example.drinkapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkapp.R;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.models.Drink;

import java.util.ConcurrentModificationException;
import java.util.List;

public class MultiChoiceAdapter extends RecyclerView.Adapter<MultiChoiceAdapter.MultiChoiceHolder>  {
    Context context;
    List<Drink> optionList;

    public MultiChoiceAdapter(Context context, List<Drink> optionList) {
        this.context = context;
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public MultiChoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.multi_check_layout,null);
        return new MultiChoiceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiChoiceHolder holder, final int position) {
        holder.checkBox.setText(optionList.get(position).Name);
        holder.txttopping.setText(new StringBuilder(optionList.get(position).Price).append("$") );

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.toppingAdded.add(buttonView.getText().toString());
                    Common.toppingPrice+=Double.parseDouble(optionList.get(position).Price);

                }else{
                    Common.toppingAdded.remove(buttonView.getText().toString());
                    Common.toppingPrice-=Double.parseDouble(optionList.get(position).Price);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public class MultiChoiceHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView txttopping;
        public MultiChoiceHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=(CheckBox) itemView.findViewById(R.id.ckb_Topping);
            txttopping=(TextView)itemView.findViewById(R.id.txt_price_topping);
        }
    }

}
