package com.ahmet.orderfoods.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.Model.User;
import com.ahmet.orderfoods.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class SignUpFragment extends Fragment {



    private TextView mTxtGoToSignIn;
    private TextInputEditText mInputName, mInputPhone, mInputPassword;
    private Button mBtnSignUp;
    private ProgressBar mProgressBar;
    private ConstraintLayout mConstraintMain;

    private DatabaseReference mReferenceSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReferenceSignUp = FirebaseDatabase.getInstance().getReference().child("User");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mTxtGoToSignIn = layoutView.findViewById(R.id.txt_go_to_sign_in);

        mInputName = layoutView.findViewById(R.id.input_name_signup);
        mInputName = layoutView.findViewById(R.id.input_name_signup);
        mInputPhone = layoutView.findViewById(R.id.input_phone_signup);
        mInputPassword = layoutView.findViewById(R.id.input_password_signup);
        mBtnSignUp = layoutView.findViewById(R.id.btn_sign_up);
        mProgressBar = layoutView.findViewById(R.id.spin_kit__sign_up);
        mConstraintMain = layoutView.findViewById(R.id.constrain_main_sign_up);

        mProgressBar.setVisibility(View.GONE);
        mConstraintMain.setVisibility(View.VISIBLE);

        return layoutView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mTxtGoToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.addFragment(new SignInFragment(), R.id.frame_layout_login,
                        getActivity().getSupportFragmentManager());

                User user = new User("ahmet", "saja");
                Bundle bundle = new Bundle();
                //bundle.putSerializable("user", user);
                bundle.putParcelable("user", user);
                setArguments(bundle);
            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar.setVisibility(View.VISIBLE);
                mConstraintMain.setVisibility(View.GONE);

                String name = mInputName.getText().toString();
                String phone = mInputPhone.getText().toString();
                String password = mInputPassword.getText().toString();


                if (Common.isConnectInternet(getActivity())) {
                    signUp(name, phone, password);
                }else {
                    Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    mConstraintMain.setVisibility(View.VISIBLE);
                    return;
                }

            }
        });
    }

    private void signUp(final String name, final String phone, final String password) {

        mReferenceSignUp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (TextUtils.isEmpty(name)) {
                    mInputName.setError("Please enter name");
                    mProgressBar.setVisibility(View.GONE);
                    mConstraintMain.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(phone)) {
                    mInputPhone.setError("Please enter phone number");
                    mProgressBar.setVisibility(View.GONE);
                    mConstraintMain.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(password)) {
                    mInputPassword.setError("Please enter password");
                    mProgressBar.setVisibility(View.GONE);
                    mConstraintMain.setVisibility(View.VISIBLE);
                } else {

                        // Check if already user phone
                        if (dataSnapshot.child(mInputPhone.getText().toString()).exists()) {

                            mProgressBar.setVisibility(View.GONE);
                            mConstraintMain.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Phone number is used by another user", Toast.LENGTH_SHORT).show();
                        } else {

                            mProgressBar.setVisibility(View.GONE);
                            mConstraintMain.setVisibility(View.VISIBLE);
                            final User user = new User(name, password);
                            mReferenceSignUp.child(phone).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgressBar.setVisibility(View.GONE);
                                                mConstraintMain.setVisibility(View.VISIBLE);
                                                Toast.makeText(getActivity(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();


                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
