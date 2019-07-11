package com.ahmet.orderfoods.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmet.orderfoods.Interface.ItmeRecyclerClickListener;
import com.ahmet.orderfoods.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView mImgCategory;
    public TextView mTxtCategoryName;

    private ItmeRecyclerClickListener itmeRecyclerClickListener;

    public CategoriesHolder(@NonNull View itemView) {
        super(itemView);

        mImgCategory = itemView.findViewById(R.id.image_category);
        mTxtCategoryName = itemView.findViewById(R.id.textcategory_name);

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
