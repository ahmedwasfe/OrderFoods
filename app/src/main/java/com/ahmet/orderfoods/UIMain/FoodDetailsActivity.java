package com.ahmet.orderfoods.UIMain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.LocaDatabase.OrdersDatabase;
import com.ahmet.orderfoods.Model.Food;
import com.ahmet.orderfoods.Model.Order;
import com.ahmet.orderfoods.R;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetailsActivity extends AppCompatActivity {

    private TextView mTxtFoodName, mTxtFoodPrice, mTxtFoodDescription;
    private ImageView mImgFood;
    private FloatingActionButton mFABCArt;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ElegantNumberButton mBtnAddMore;

    private String foodId = "";
    private Food mCurrentFood;

    private DatabaseReference mReferenceFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        mReferenceFood = FirebaseDatabase.getInstance().getReference().child("Foods");

        mTxtFoodName = findViewById(R.id.txt_name_details);
        mTxtFoodPrice = findViewById(R.id.txt_price_details);
        mTxtFoodDescription = findViewById(R.id.txt_description_details);
        mImgFood = findViewById(R.id.img_food_details);
        mFABCArt = findViewById(R.id.fab_add_cart);
        mCollapsingToolbar = findViewById(R.id.collapse);
        mBtnAddMore = findViewById(R.id.btn_add_more);

        mCollapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        mCollapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsAppbar);

        mFABCArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OrdersDatabase(FoodDetailsActivity.this)
                        .addCarts(new Order(
                                foodId,
                                mCurrentFood.getName(),
                                mBtnAddMore.getNumber(),
                                mCurrentFood.getPrice(),
                                mCurrentFood.getDiscount()
                        ));
                Toast.makeText(FoodDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnAddMore.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(FoodDetailsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Get food id
        if (getIntent() != null){
            foodId = getIntent().getStringExtra("foodId");
        }
        if (!foodId.isEmpty() && foodId != null){

            if (Common.isConnectInternet(this)) {
                getFoodDetails(foodId);
            }else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void getFoodDetails(final String foodId) {

        mReferenceFood.child(foodId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mCurrentFood = dataSnapshot.getValue(Food.class);

                        Picasso.get()
                                .load(mCurrentFood.getImage())
                                .placeholder(R.drawable.background_main)
                                .into(mImgFood);

                        mCollapsingToolbar.setTitle(mCurrentFood.getName());
                        mTxtFoodName.setText(mCurrentFood.getName());
                        mTxtFoodPrice.setText("$" + mCurrentFood.getPrice());
                        mTxtFoodDescription.setText(mCurrentFood.getDescription());

                       // Log.d("DESCRIPTION",food.getDescrription());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(FoodDetailsActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
