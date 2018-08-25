package com.example.ndamulelomasindi.taxiapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class DriverActivity extends AppCompatActivity {

    TextView tvAmount, tvPassengers, tvCollected;
    ImageView ivMin, ivAdd;
    Button btnSave;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_activity);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvAmount = findViewById(R.id.tvAmount);
        tvPassengers = findViewById(R.id.tvPassengers);
        ivMin = findViewById(R.id.ivMin);
        ivAdd = findViewById(R.id.ivAdd);
        btnSave = findViewById(R.id.btnSave);
        tvCollected = findViewById(R.id.tvCollected);

        tvCollected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgress(true);
                tvLoad.setText("Logging you out...please wait...");

                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Toast.makeText(DriverActivity.this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DriverActivity.this, LoginActivity.class));
                        DriverActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(DriverActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
            }
        });

        tvAmount.setText("R" + MbassSetup.user.getProperty("amount") + "");
        tvPassengers.setText(MbassSetup.user.getProperty("passengers") + "");

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MbassSetup.user.setProperty("amount", (int) MbassSetup.user.getProperty("amount") + 8);

                MbassSetup.user.setProperty("passengers", (int) MbassSetup.user.getProperty("passengers") + 1);

                tvAmount.setText("R" + MbassSetup.user.getProperty("amount") + "");
                tvPassengers.setText(MbassSetup.user.getProperty("passengers") + "");

            }
        });

        ivMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MbassSetup.user.setProperty("amount", (int) MbassSetup.user.getProperty("amount") - 8);
                MbassSetup.user.setProperty("passengers", (int) MbassSetup.user.getProperty("passengers") - 1);

                if ((int)MbassSetup.user.getProperty("amount") < 0)
                {
                    MbassSetup.user.setProperty("amount", 0);
                    MbassSetup.user.setProperty("passengers", 0);
                }

                tvAmount.setText("R" + MbassSetup.user.getProperty("amount") + "");
                tvPassengers.setText(MbassSetup.user.getProperty("passengers") + "");

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgress(true);
                tvLoad.setText("Saving data...please wait...");

                Backendless.Persistence.save(MbassSetup.user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {

                        Toast.makeText(DriverActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(DriverActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
            }
        });


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
