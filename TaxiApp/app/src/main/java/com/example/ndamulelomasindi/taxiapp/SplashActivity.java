package com.example.ndamulelomasindi.taxiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class SplashActivity extends AppCompatActivity {

    TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        tvLoad = findViewById(R.id.tvLoad);

        tvLoad.setText(R.string.checking_credentials);

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {

                if (response)
                {
                    String userObjectId = UserIdStorageFactory.instance().getStorage().get();

                    tvLoad.setText(R.string.valid_credentials);

                    Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            MbassSetup.user = response;

                            if (response.getProperty("role").equals("Admin"))
                            {
                                startActivity(new Intent(SplashActivity.this, AdminActivity.class));
                            }
                            else
                            {
                                startActivity(new Intent(SplashActivity.this, DriverActivity.class));
                            }

                            SplashActivity.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(SplashActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            SplashActivity.this.finish();

                        }
                    });
                }
                else
                {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(SplashActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                SplashActivity.this.finish();
            }
        });
    }
}
