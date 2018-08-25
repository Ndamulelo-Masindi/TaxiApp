package com.example.ndamulelomasindi.taxiapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RegisterActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etName, etMail, etPassword, etReEnter;
    Button btnRegister;
    RadioButton rbAdmin, rbDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etName = findViewById(R.id.etName);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etReEnter = findViewById(R.id.etReEnter);
        btnRegister = findViewById(R.id.btnRegister);
        rbAdmin = findViewById(R.id.rbAdmin);
        rbDriver = findViewById(R.id.rbDriver);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().isEmpty() || etMail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() ||
                        etReEnter.getText().toString().isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, R.string.enter_all_details, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (etPassword.getText().toString().trim().equals(etReEnter.getText().toString().trim()))
                    {
                        String name = etName.getText().toString().trim();
                        String email = etMail.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();

                        BackendlessUser user = new BackendlessUser();
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setProperty("name", name);

                        if (rbAdmin.isChecked())
                        {
                            user.setProperty("role", "Admin");
                        }
                        else
                        {
                            user.setProperty("passengers", 0);

                            user.setProperty("amount", 0.0);

                            user.setProperty("role", "Driver");
                        }

                        showProgress(true);
                        tvLoad.setText(R.string.busy_registering);

                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {

                                Toast.makeText(RegisterActivity.this, R.string.successfully_registered, Toast.LENGTH_SHORT).show();
                                RegisterActivity.this.finish();

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                                Toast.makeText(RegisterActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, R.string.passwords_same, Toast.LENGTH_SHORT).show();
                    }
                }

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
