package com.ahmet.orderfoods.UIMain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.Holder.FoodHolder;
import com.ahmet.orderfoods.Interface.ItmeRecyclerClickListener;
import com.ahmet.orderfoods.Model.Food;
import com.ahmet.orderfoods.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {

    private RecyclerView mRecyclerFood;
    private ProgressBar mProgressBar;
    private MaterialSearchBar mSearchBar;

    private DatabaseReference mReferenceFood;
    private FirebaseRecyclerAdapter<Food, FoodHolder> mFoodAdapter;
    private FirebaseRecyclerOptions<Food> mFoodOptions;

    private FirebaseRecyclerAdapter<Food, FoodHolder> mSearchFoodAdapter;
    private FirebaseRecyclerOptions<Food> mSearchFoodOptions;
    private List<String> mSearchList = new ArrayList<>();


    private String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        mRecyclerFood = findViewById(R.id.recycler_foods);
        mRecyclerFood.setHasFixedSize(true);
        mRecyclerFood.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));

        mProgressBar = findViewById(R.id.spin_kit__food);
        Sprite wave = new Wave();
        mProgressBar.setIndeterminateDrawable(wave);
        mProgressBar.setVisibility(View.VISIBLE);


        mReferenceFood = FirebaseDatabase.getInstance().getReference().child("Foods");

        if (getIntent() != null){
            categoryId = getIntent().getStringExtra("categoryId");
        }

        if (!categoryId.isEmpty() && categoryId != null){
            if (Common.isConnectInternet(this)) {
                loadFoods(categoryId);
            }else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        mSearchBar = findViewById(R.id.mt_search_bar);
        mSearchBar.setHint("Search...");
        loadSuggestions();
        mSearchBar.setLastSuggestions(mSearchList);
        mSearchBar.setCardViewElevation(10);
        mSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // When user type their text, will change suggest list
                List<String> mListSuggest = new ArrayList<>();
                for (String search : mSearchList){
                    if (search.toLowerCase().contains(mSearchBar.getText().toLowerCase()))
                        mListSuggest.add(search);
                }

                mSearchBar.setLastSuggestions(mListSuggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

                // When user type their text, will change suggest list
                List<String> mListSuggest = new ArrayList<>();
                for (String search : mSearchList){
                    if (search.toLowerCase().contains(mSearchBar.getText().toLowerCase()))
                        mListSuggest.add(search);
                }

                mSearchBar.setLastSuggestions(mListSuggest);
            }
        });
        mSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                // When Search bar is close
                // Restore original adapter
                if (!enabled){
                    mRecyclerFood.setAdapter(mFoodAdapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                // When search finish
                // Show result of search adapter
                searchForFoods(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


    }

    private void searchForFoods(CharSequence wordSearch) {

        Query searchQuery = mReferenceFood.orderByChild("name").equalTo(wordSearch.toString());
        mSearchFoodOptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchQuery, Food.class)
                .build();

        mSearchFoodAdapter = new FirebaseRecyclerAdapter<Food, FoodHolder>(mSearchFoodOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FoodHolder foodHolder, int position, @NonNull Food food) {

                mProgressBar.setVisibility(View.GONE);

                foodHolder.mTxtFoodName.setText(food.getName());

                Picasso.get()
                        .load(food.getImage())
                        .placeholder(R.drawable.background_main)
                        .into(foodHolder.mImgFood);

                foodHolder.setItmeRecyclerClickListener(new ItmeRecyclerClickListener() {
                    @Override
                    public void onItemClickListener(View view, int posiotn, boolean isLongClick) {
                        // Toast.makeText(FoodActivity.this, posiotn + " " +
                        //         food.getName(), Toast.LENGTH_SHORT).show();
                        Intent detailsIntent = new Intent(FoodActivity.this, FoodDetailsActivity.class);
                        detailsIntent.putExtra("foodId", mSearchFoodAdapter.getRef(posiotn).getKey());
                        startActivity(detailsIntent);
                    }
                });

            }

            @NonNull
            @Override
            public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View layoutView = LayoutInflater.from(FoodActivity.this)
                        .inflate(R.layout.raw_food, parent, false);
                return new FoodHolder(layoutView);
            }
        };
    mRecyclerFood.setAdapter(mSearchFoodAdapter);

    }

    private void loadFoods(String categoryId) {

        Query foodQuery = mReferenceFood.orderByChild("categoryId").equalTo(categoryId);
        mFoodOptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodQuery, Food.class)
                .build();

        mFoodAdapter = new FirebaseRecyclerAdapter<Food, FoodHolder>(mFoodOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FoodHolder foodHolder, int i, @NonNull final Food food) {

                mProgressBar.setVisibility(View.GONE);

                foodHolder.mTxtFoodName.setText(food.getName());

                Picasso.get()
                        .load(food.getImage())
                        .placeholder(R.drawable.background_main)
                        .into(foodHolder.mImgFood);

                foodHolder.setItmeRecyclerClickListener(new ItmeRecyclerClickListener() {
                    @Override
                    public void onItemClickListener(View view, int posiotn, boolean isLongClick) {
                       // Toast.makeText(FoodActivity.this, posiotn + " " +
                       //         food.getName(), Toast.LENGTH_SHORT).show();
                        Intent detailsIntent = new Intent(FoodActivity.this, FoodDetailsActivity.class);
                        detailsIntent.putExtra("foodId", mFoodAdapter.getRef(posiotn).getKey());
                        startActivity(detailsIntent);
                    }
                });
            }

            @NonNull
            @Override
            public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View layoutView = LayoutInflater.from(FoodActivity.this)
                        .inflate(R.layout.raw_food, parent, false);
                return new FoodHolder(layoutView);
            }

        };

        mRecyclerFood.setAdapter(mFoodAdapter);
    }

    private void loadSuggestions(){

        mReferenceFood.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Food searchFood = snapshot.getValue(Food.class);
                            mSearchList.add(searchFood.getName());  // Add name of food to suggest list
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mFoodAdapter.startListening();
  //      mSearchFoodAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mFoodAdapter.stopListening();
    //    mSearchFoodAdapter.stopListening();
    }
}
