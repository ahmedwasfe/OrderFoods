package com.ahmet.orderfoods.Holder;

import android.view.View;
import android.widget.TextView;

import com.ahmet.orderfoods.Interface.ItmeRecyclerClickListener;
import com.ahmet.orderfoods.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderStatusHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mTxtOrderId, mTxtOrderStatus, mTxtOrderPhone, mTxtOrderAddress;

    private ItmeRecyclerClickListener itmeRecyclerClickListener;

    public OrderStatusHolder(@NonNull View itemView) {
        super(itemView);

        mTxtOrderId = itemView.findViewById(R.id.txt_order_status_Id);
        mTxtOrderStatus = itemView.findViewById(R.id.txt_order_status);
        mTxtOrderPhone = itemView.findViewById(R.id.txt_order_status_phone);
        mTxtOrderAddress = itemView.findViewById(R.id.txt_order_status_address);

        itemView.setOnClickListener(this);
    }

    public void setItmeRecyclerClickListener(ItmeRecyclerClickListener itmeRecyclerClickListener) {
        this.itmeRecyclerClickListener = itmeRecyclerClickListener;
    }

    @Override
    public void onClick(View v) {
        itmeRecyclerClickListener.onItemClickListener(v, getAdapterPosition(), false);
    }
}
