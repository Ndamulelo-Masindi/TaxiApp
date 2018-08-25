package com.example.ndamulelomasindi.taxiapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    ListView lvList;
    TextView tvTotal;
    ArrayList<Drivers> drivers;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        lvList = findViewById(R.id.lvList);
        tvTotal = findViewById(R.id.tvTotal);

        drivers = new ArrayList<Drivers>();

        tvTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgress(true);
                tvLoad.setText("Logging you out...please wait...");

                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Toast.makeText(AdminActivity.this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                        AdminActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(AdminActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });

            }
        });

        String whereClause = "role = 'Driver'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("name");

        showProgress(true);
        tvLoad.setText("Getting all drivers...please wait...");

        Backendless.Persistence.of(BackendlessUser.class).find(queryBuilder, new AsyncCallback<List<BackendlessUser>>() {
            @Override
            public void handleResponse(List<BackendlessUser> response) {

                int total = 0;
                Drivers driver;

                for (int i = 0 ; i < response.size() ; i++)
                {
                    total += (int) response.get(i).getProperty("amount");
                    driver = new Drivers();
                    driver.setAmount((int)response.get(i).getProperty("amount"));
                    driver.setName(response.get(i).getProperty("name")+"");
                    drivers.add(driver);

                }

                tvTotal.setText("Total amount: R" + total);

                TaxiAdapter adapter = new TaxiAdapter(AdminActivity.this, drivers);
                lvList.setAdapter(adapter);

                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(AdminActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
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
