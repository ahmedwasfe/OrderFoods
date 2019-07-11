package com.ahmet.orderfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.Fragments.SignInFragment;
import com.ahmet.orderfoods.UIMain.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Common.mCurrentUser != null){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        Common.addFragment(new SignInFragment(), R.id.frame_layout_login, getSupportFragmentManager());


    }
}
