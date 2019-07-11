package com.ahmet.orderfoods.UIMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.Holder.OrderStatusHolder;
import com.ahmet.orderfoods.Interface.ItmeRecyclerClickListener;
import com.ahmet.orderfoods.Model.Request;
import com.ahmet.orderfoods.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class OrderStatusActivity extends AppCompatActivity {

    private RecyclerView mRecyclerOrderStatus;
    private ProgressBar mProgressBar;

    private DatabaseReference mReferenceOrderStatus;
    private FirebaseRecyclerOptions<Request> mRequestOptions;
    private FirebaseRecyclerAdapter<Request, OrderStatusHolder> mRequestAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        mReferenceOrderStatus = FirebaseDatabase.getInstance().getReference().child("Request");

        mRecyclerOrderStatus = findViewById(R.id.recycler_order_status);
        mRecyclerOrderStatus.setHasFixedSize(true);
        mRecyclerOrderStatus.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));

        mProgressBar = findViewById(R.id.spin_kit__order_status);
        Sprite wave = new Wave();
        mProgressBar.setIndeterminateDrawable(wave);
        mProgressBar.setVisibility(View.VISIBLE);

        loadOrderStatus(Common.mCurrentUser.getPhone());
        // If we start Order Status activity From Home Activity
        // Will not put any extra so we just load order by phone from common
//        if (getIntent() == null) {
//            loadOrderStatus(Common.mCurrentUser.getPhone());
//        }else {
//            loadOrderStatus(getIntent().getStringExtra("userPhone"));
//        }
    }


    private void loadOrderStatus(String phone) {

        Query query = mReferenceOrderStatus.orderByChild("phone").equalTo(phone);

        mRequestOptions = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        mRequestAdapter = new FirebaseRecyclerAdapter<Request, OrderStatusHolder>(mRequestOptions) {
            @Override
            protected void onBindViewHolder(@NonNull OrderStatusHolder orderStatusHolder, int position, @NonNull Request request) {

                mProgressBar.setVisibility(View.GONE);

                orderStatusHolder.mTxtOrderId.setText(mRequestAdapter.getRef(position).getKey());
                orderStatusHolder.mTxtOrderAddress.setText(request.getAddress());
                orderStatusHolder.mTxtOrderPhone.setText(request.getPhone());
                orderStatusHolder.mTxtOrderStatus.setText(Common.convertStatus(request.getStatus()));

                orderStatusHolder.setItmeRecyclerClickListener(new ItmeRecyclerClickListener() {
                    @Override
                    public void onItemClickListener(View view, int posiotn, boolean isLongClick) {
                        // coe later
                    }
                });
            }

            @NonNull
            @Override
            public OrderStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View layoutView = LayoutInflater.from(OrderStatusActivity.this)
                        .inflate(R.layout.raw_order_status, parent, false);

                return new OrderStatusHolder(layoutView);
            }
        };

//        mRequestAdapter.notifyDataSetChanged();
        mRecyclerOrderStatus.setAdapter(mRequestAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        mRequestAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRequestAdapter.stopListening();
    }
}
