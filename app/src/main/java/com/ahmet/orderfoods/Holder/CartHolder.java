package com.ahmet.orderfoods.Holder;

import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.Interface.ItmeRecyclerClickListener;
import com.ahmet.orderfoods.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener {


    public TextView mTxtFoodName, mTxtFoodPrice;
    public ImageView mImgCartCount;

    private ItmeRecyclerClickListener itmeRecyclerClickListener;

    public CartHolder(@NonNull View itemView) {
        super(itemView);

        mTxtFoodName = itemView.findViewById(R.id.txt_food_name_cart);
        mTxtFoodPrice = itemView.findViewById(R.id.txt_food_price_cart);
        mImgCartCount = itemView.findViewById(R.id.img_cart_count);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle(Common.Select_Action);
        menu.add(0,0, getAdapterPosition(), Common.DELETE);
    }
}
