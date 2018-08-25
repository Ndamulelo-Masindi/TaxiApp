package com.example.ndamulelomasindi.taxiapp;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class MbassSetup extends Application {

    public static final String APPLICATION_ID = "4B278B4F-561C-3958-FFE9-53986FA5B600";
    public static final String API_KEY = "2B1184AF-F136-36D2-FF70-A5A185624400";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );

    }
}
