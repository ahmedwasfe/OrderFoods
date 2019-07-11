package com.ahmet.orderfoods.UIMain;

import android.content.Intent;
import android.os.Bundle;

import com.ahmet.orderfoods.Common.Common;
import com.ahmet.orderfoods.Fragments.CartFragment;
import com.ahmet.orderfoods.Fragments.CategoriesFragment;
import com.ahmet.orderfoods.MainActivity;
import com.ahmet.orderfoods.R;
import com.ahmet.orderfoods.Service.ListenOrderService;
import com.ahmet.orderfoods.UIMain.OrderStatusActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.paperdb.Paper;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mTxtName;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Client App");
        setSupportActionBar(toolbar);

        Common.addFragment(new CategoriesFragment(), R.id.frame_layout_home, getSupportFragmentManager());

        Paper.init(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // get Name user
        View headerView = navigationView.getHeaderView(0);
        mTxtName = headerView.findViewById(R.id.txt_name);
        mTxtName.setText(Common.mCurrentUser.getName());

        Common.createNotificationChannel(HomeActivity.this);

        // Register Sevice
        Intent service = new Intent(this, ListenOrderService.class);
        startService(service);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Common.addFragment(new CategoriesFragment(), R.id.frame_layout_home, getSupportFragmentManager());
            fab.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_cart) {
            Common.addFragment(new CartFragment(), R.id.frame_layout_home, getSupportFragmentManager());
            fab.setVisibility(View.GONE);

        } else if (id == R.id.nav_order) {
            startActivity(new Intent(HomeActivity.this, OrderStatusActivity.class));
            fab.setVisibility(View.GONE);

        } else if (id == R.id.nav_sign_out) {

            // Delete Remember phone and password
            Paper.book().destroy();

            // Log out
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
