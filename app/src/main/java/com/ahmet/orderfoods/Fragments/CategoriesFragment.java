package com.ahmet.orderfoods.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.UIMain.FoodActivity;
import com.ahmet.orderfoods.Holder.CategoriesHolder;
import com.ahmet.orderfoods.Interface.ItmeRecyclerClickListener;
import com.ahmet.orderfoods.Model.Categories;
import com.ahmet.orderfoods.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CategoriesFragment extends Fragment {

    // init views
    private RecyclerView mRecyclerCategores;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefresh;

    private DatabaseReference mReferenceCategores;
    private FirebaseRecyclerOptions<Categories> mCategoresOptions;
    private FirebaseRecyclerAdapter<Categories, CategoriesHolder> mCategoresAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init Firebase
        mReferenceCategores = FirebaseDatabase.getInstance().getReference().child("Categories");

        // Query categoryQuery = mReferenceCategores.orderByChild("name");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_categories, container, false);

        mRecyclerCategores = layoutView.findViewById(R.id.recycler_categores);
        mRecyclerCategores.setHasFixedSize(true);
        mRecyclerCategores.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));

        mSwipeRefresh = layoutView.findViewById(R.id.swipe_refresh_categories);

        // init Progress
        mProgressBar = layoutView.findViewById(R.id.spin_kit__home);
        Sprite wave = new Wave();
        mProgressBar.setIndeterminateDrawable(wave);

        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerCategores.setVisibility(View.VISIBLE);

        return layoutView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Common.isConnectInternet(getActivity())) {
            loadCategores();
        }else {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectInternet(getActivity())) {
                    loadCategores();
                }else {
                    Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void loadCategores() {


        mCategoresOptions = new FirebaseRecyclerOptions.Builder<Categories>()
                .setQuery(mReferenceCategores, Categories.class)
                .build();


        mCategoresAdapter = new FirebaseRecyclerAdapter<Categories, CategoriesHolder>(mCategoresOptions) {
            @Override
            protected void onBindViewHolder(@NonNull CategoriesHolder categoriesHolder, final int position, @NonNull final Categories categories) {

                mProgressBar.setVisibility(View.GONE);
                mRecyclerCategores.setVisibility(View.VISIBLE);

                categoriesHolder.mTxtCategoryName.setText(categories.getName());


                Picasso.get()
                        .load(categories.getImage())
                        .placeholder(R.drawable.background_main)
                        .into(categoriesHolder.mImgCategory);

                categoriesHolder.setItmeRecyclerClickListener(new ItmeRecyclerClickListener() {
                    @Override
                    public void onItemClickListener(View view, int posiotn, boolean isLongClick) {

                        // Get categoryId and send to food Activity
                        Intent foodIntent = new Intent(getActivity(), FoodActivity.class);
                        // Becouse categoryId is key so we just get key of this item
                        foodIntent.putExtra("categoryId", mCategoresAdapter.getRef(position).getKey());
                        startActivity(foodIntent);
                    }
                });
            }

            @NonNull
            @Override
            public CategoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View layoutView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.raw_category, parent, false);

                return new CategoriesHolder(layoutView);
            }
        };

        mCategoresAdapter.notifyDataSetChanged();
        mSwipeRefresh.setRefreshing(false);
        mRecyclerCategores.setAdapter(mCategoresAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        mCategoresAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mCategoresAdapter.stopListening();
    }

}

