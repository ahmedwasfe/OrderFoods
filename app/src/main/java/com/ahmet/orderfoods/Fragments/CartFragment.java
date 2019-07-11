package com.ahmet.orderfoods.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmet.orderfoods.Adapter.CartAdapter;
import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.LocaDatabase.OrdersDatabase;
import com.ahmet.orderfoods.Model.Order;
import com.ahmet.orderfoods.Model.Request;
import com.ahmet.orderfoods.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class CartFragment extends Fragment {

    private RecyclerView mRecyclerCart;
    private TextView mTxtTotalPrice;
    private Button mBtnPlaceOrder;

    private DatabaseReference mReferenceCart;

    private List<Order> mListOrder;
    private CartAdapter mCartAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReferenceCart = FirebaseDatabase.getInstance().getReference().child("Request");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_cart, container, false);

        mRecyclerCart = layoutView.findViewById(R.id.recycler_cart);
        mRecyclerCart.setHasFixedSize(true);
        mRecyclerCart.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));

        mTxtTotalPrice = layoutView.findViewById(R.id.txt_total_price);
        mBtnPlaceOrder = layoutView.findViewById(R.id.btn_place_order);

        return layoutView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Common.isConnectInternet(getActivity())) {
            loadOrders();
        }else {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        mBtnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListOrder.size() > 0) {
                    submitRequest();
                }else {
                    Toast.makeText(getActivity(), "Ypur cart is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void submitRequest() {

        // init dialog
        final BottomSheetDialog mSheetDialog = new BottomSheetDialog(getActivity());
        mSheetDialog.setCancelable(false);
        mSheetDialog.setCanceledOnTouchOutside(false);

        View sheetDialogView = getLayoutInflater().inflate(R.layout.sheet_dialog_place_order, null);

        final TextInputEditText mInputAddress = sheetDialogView.findViewById(R.id.input_address);
        Button mBtnSubmitRequest = sheetDialogView.findViewById(R.id.btn_submit_request);
        ImageButton mDissmisDialog = sheetDialogView.findViewById(R.id.img_dissmis_dialog);

        mBtnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = mInputAddress.getText().toString();
               // Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                // Create new Reuest
                Request request = new Request(
                        Common.mCurrentUser.getPhone(),
                        Common.mCurrentUser.getName(),
                        address,
                        mTxtTotalPrice.getText().toString(),
                        mListOrder
                );

                // Add to firebSe
                // will use System.currentTimeMillis to key
                mReferenceCart.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    new OrdersDatabase(getActivity()).clearCarts();
                                    mCartAdapter.notifyDataSetChanged();
                                    mListOrder.clear();
                                    mTxtTotalPrice.setText("$0.00");
                                    Toast.makeText(getActivity(), "Thank You, Order Olaced", Toast.LENGTH_SHORT).show();
                                    mSheetDialog.dismiss();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        mSheetDialog.dismiss();
                    }
                });

            }
        });

        mDissmisDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetDialog.dismiss();
            }
        });

        mSheetDialog.setContentView(sheetDialogView);
        mSheetDialog.show();
    }

    private void loadOrders() {

        mListOrder = new OrdersDatabase(getActivity()).getOrders();

        mCartAdapter = new CartAdapter(getActivity(), mListOrder);
        mCartAdapter.notifyDataSetChanged();
        mRecyclerCart.setAdapter(mCartAdapter);

        // Calculate total price
        int total = 0;
        for (Order order : mListOrder){
            total += (Integer.parseInt(order.getPrice()))
                    * (Integer.parseInt(order.getQuantity()));
        }

        Locale locale = new Locale("en", "US");
        NumberFormat numFormat = NumberFormat.getCurrencyInstance(locale);
        mTxtTotalPrice.setText(numFormat.format(total));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals(Common.DELETE)){
            deleteFromCart(item.getOrder());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteFromCart(int position) {

        // Will delete from list by position
        mListOrder.remove(position);

        // After that will delete all old data from database
        new  OrdersDatabase(getActivity()).clearCarts();
        for (Order order : mListOrder)
            new OrdersDatabase(getActivity()).addCarts(order);
        loadOrders();

        // After that will delete Order by posiotn from database
//        new OrdersDatabase(getActivity()).clearFromCart(position);
//        loadOrders();
    }
}
