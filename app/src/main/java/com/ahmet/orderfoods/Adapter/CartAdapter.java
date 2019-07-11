package com.ahmet.orderfoods.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmet.orderfoods.Holder.CartHolder;
import com.ahmet.orderfoods.Model.Order;
import com.ahmet.orderfoods.R;
import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartHolder> {

    private Context mContext;
    private List<Order> mListOrder;
    private LayoutInflater inflater;

    public CartAdapter(Context mContext, List<Order> mListOrder) {
        this.mContext = mContext;
        this.mListOrder = mListOrder;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = inflater.inflate(R.layout.raw_cart, parent, false);

        return new CartHolder(layoutView);

    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {

        holder.mTxtFoodName.setText(mListOrder.get(position).getProductName());

        TextDrawable mTextDrawable = TextDrawable.builder()
                .buildRound("" + mListOrder.get(position).getQuantity(), Color.DKGRAY);
        holder.mImgCartCount.setImageDrawable(mTextDrawable);


        Locale locale = new Locale("en", "US");
        NumberFormat numFormat = NumberFormat.getCurrencyInstance(locale);

        int mPrice = (Integer.parseInt(mListOrder.get(position).getPrice()))
                * (Integer.parseInt(mListOrder.get(position).getQuantity()));

        holder.mTxtFoodPrice.setText(numFormat.format(mPrice));

    }

    @Override
    public int getItemCount() {
        return mListOrder.size();
    }
}
