package com.ahmet.orderfoods.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmet.orderfoods.Interface.ItmeRecyclerClickListener;
import com.ahmet.orderfoods.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoodHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView mImgFood;
    public TextView mTxtFoodName;

    private ItmeRecyclerClickListener itmeRecyclerClickListener;

    public FoodHolder(@NonNull View itemView) {
        super(itemView);

        mImgFood = itemView.findViewById(R.id.image_food);
        mTxtFoodName = itemView.findViewById(R.id.textfood_name);

        itemView.setOnClickListener(this);
    }

    public void setItmeRecyclerClickListener(ItmeRecyclerClickListener itmeRecyclerClickListener) {
        this.itmeRecyclerClickListener = itmeRecyclerClickListener;
    }

    @Override
    public void onClick(View view) {
        itmeRecyclerClickListener.onItemClickListener(view, getAdapterPosition(), false);

    }
}
