package com.ahmet.orderfoods.Common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.ahmet.orderfoods.Model.User;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class Common {

    public static User mCurrentUser;


    // update and Delete
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String Select_Action = "Select the action";

    // Remember User
    public static final String USER_KEY = "Phone";
    public static final String PASSWORD_KEY = "Password";

    // Channel Id only api 28 android P
    public static final String CHANNEL_ID = "channel ID";
    public static final int NOTIFICATION_ID = 1;


    public static void addFragment(Fragment fragment, int id, FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .replace(id, fragment)
                .commit();
    }

    public static String convertStatus(String status) {

        if (status.equals("0")){
            return "Placed";
        }else if (status.equals("1")){
            return "On my way";
        }

        return "Shipped";
    }


    public static void createNotificationChannel(Context context){


        CharSequence name = "Channel name";
        String description = "Channel description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            // notificationChannel.setShowBadge(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    // Check intenet connection
    public static boolean isConnectInternet(Context context){

        ConnectivityManager Connectmanager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        if (Connectmanager != null){

            NetworkInfo[] info = Connectmanager.getAllNetworkInfo();

            for (int i = 0; i < info.length; i++){

                if (info[i].getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
}
