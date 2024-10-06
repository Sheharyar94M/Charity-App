package com.example.charity.authentication.sign_up;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.charity.Model.PersonNeed;
import com.example.charity.R;
import com.example.charity.authentication.login.LoginActivity;
import com.example.charity.databinding.ActivitySignUpBinding;
import com.example.charity.preferences.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignUpBinding signUpBinding;
    private boolean isLoggedIn = false;
    private String username;
    private String password;
    private String confirm_password;
    private String fullname;
    private String contact;
    private PersonNeed personNeed;
    private FirebaseAuth auth;
    private PreferenceManager preferenceManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());

        setContentView(signUpBinding.getRoot());

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.start_button_color));

        preferenceManager = new PreferenceManager(this);

        auth = FirebaseAuth.getInstance();


        signUpBinding.loginLink.setOnClickListener(v -> {

            finish();
        });


        signUpBinding.signupButton.setOnClickListener(v -> {

            if (!Objects.requireNonNull(signUpBinding.usernameEdt.getText()).toString().isEmpty())
                username = signUpBinding.usernameEdt.getText().toString();
            else
                signUpBinding.usernameEdt.setError("Please enter email");

            if (!Objects.requireNonNull(signUpBinding.fullnameEdt.getText()).toString().isEmpty())
                fullname = signUpBinding.fullnameEdt.getText().toString();
            else
                signUpBinding.fullnameEdt.setError("Please enter fullname");

            if (!Objects.requireNonNull(signUpBinding.passwordEdt.getText()).toString().isEmpty())
                password = signUpBinding.passwordEdt.getText().toString();
            else
                signUpBinding.passwordEdt.setError("Please enter password");

            if (!Objects.requireNonNull(signUpBinding.confirmPasswordEdt.getText()).toString().isEmpty())
                confirm_password = signUpBinding.confirmPasswordEdt.getText().toString();
            else
                signUpBinding.confirmPasswordEdt.setError("Please enter password");

            if (!Objects.requireNonNull(signUpBinding.contactEdt.getText()).toString().isEmpty()) {

                if (signUpBinding.contactEdt.getText().toString().length() == 11) {

                    contact = signUpBinding.contactEdt.getText().toString();

                } else {

                    signUpBinding.contactEdt.setError("Invalid contact!");
                }
            } else
                signUpBinding.contactEdt.setError("Please enter contact");

            if (signUpBinding.passwordEdt.getText() != null
                    && signUpBinding.usernameEdt.getText() != null
                    && signUpBinding.fullnameEdt.getText() != null
                    && signUpBinding.contactEdt.getText() != null) {


                if (password != null && confirm_password != null) {

                    if (password.equals(confirm_password)) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        ref.keepSynced(true);

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    PersonNeed personNeed = dataSnapshot.getValue(PersonNeed.class);

                                    try {

                                        if (personNeed != null){

                                            if (personNeed.getEmail().equalsIgnoreCase(username)) {

                                                isLoggedIn = true;

                                            } else {

                                                isLoggedIn = false;

                                            }
                                        }else{

                                            isLoggedIn = false;

                                        }
                                    }catch(Exception error){

                                        Log.d("Null Database user", error.getLocalizedMessage());

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                throw error.toException();

                            }
                        });

                        if (!isLoggedIn) {

                            assert signUpBinding.alreadyExists != null;
                            signUpBinding.alreadyExists.setVisibility(View.GONE);

                            if (username != null) {

                                if (signUpBinding.usernameEdt.getText().toString().contains("@")) {

                                    if (!preferenceManager.isLoggedIn()) {

                                        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, task -> {

                                            if (task.isSuccessful()) {

                                                personNeed = new PersonNeed(fullname, username, contact, password, String.valueOf(Long.MAX_VALUE - System.currentTimeMillis()));

                                                //realtime database
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Users")
                                                        .child("Needy_person")
                                                        .child(personNeed.getId())
                                                        .setValue(personNeed);

                                                preferenceManager.storeuser(personNeed);

                                                Log.d("signup_user",personNeed.toString());

                                                //Snack bar
                                                View parentLayout = findViewById(android.R.id.content);
                                                Snackbar snackbar = Snackbar.make(parentLayout, "Signup Successful", Snackbar.LENGTH_LONG);
                                                snackbar.setAction("Close", view -> {

                                                    snackbar.dismiss();
                                                });

                                                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                                                snackbar.show();

                                                //signin
                                                Intent login = new Intent(SignupActivity.this, LoginActivity.class);
                                                login.putExtra("signup_in_user", (Serializable) personNeed);
                                                startActivity(login);
                                                finish();

                                            }

                                        });

                                    } else {

                                        signUpBinding.alreadyExists.setVisibility(View.VISIBLE);
                                    }

                                } else
                                    signUpBinding.usernameEdt.setError("Invalid email");


                            } else {

                                signUpBinding.usernameEdt.setError("Please enter email");

                            }


                        } else {

                            signUpBinding.alreadyExists.setVisibility(View.VISIBLE);
                        }

                    } else {

                        signUpBinding.passwordEdt.setError("Passwords do not match!");
                    }
                } else {

                    signUpBinding.passwordEdt.setError("Please enter password");
                    signUpBinding.confirmPasswordEdt.setError("Please confirm password");

                }
            } else {

                signUpBinding.usernameEdt.setError("Empty fields are not allowed");
                signUpBinding.passwordEdt.setError("Empty fields are not allowed");
                signUpBinding.contactEdt.setError("Empty fields are not allowed");
                signUpBinding.fullnameEdt.setError("Empty fields are not allowed");
                signUpBinding.confirmPasswordEdt.setError("Empty fields are not allowed");
            }

        });


        assert signUpBinding.hidePasswordSignup1 != null;
        signUpBinding.hidePasswordSignup1.setOnClickListener(this::ShowHidePass);

    }

    public void ShowHidePass(View view){

        if(view.getId()==R.id.hide_password_login){

            if(signUpBinding.passwordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                assert signUpBinding.hidePasswordSignup1 != null;
                signUpBinding.hidePasswordSignup1.setImageResource(R.drawable.hide_password);

                //Show Password
                signUpBinding.passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                assert signUpBinding.hidePasswordSignup1 != null;
                signUpBinding.hidePasswordSignup1.setImageResource(R.drawable.show_password);

                //Hide Password
                signUpBinding.passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

}