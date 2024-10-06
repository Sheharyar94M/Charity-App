package com.example.ultimatebook.authentication.sign_up;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ultimatebook.authentication.login.WantLoginActivity;
import com.example.ultimatebook.databinding.ActivitySignupBinding;
import com.example.ultimatebook.i_want.ui.home.HomeWantActivity;
import com.example.ultimatebook.model.HelpingPerson;
import com.example.ultimatebook.preferences.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding signupBinding;
    private FirebaseAuth auth;
    private PreferenceManager preferenceManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signupBinding = ActivitySignupBinding.inflate(getLayoutInflater());

        setContentView(signupBinding.getRoot());

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(com.example.charity.R.color.start_button_color));

        auth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(this);

        signupBinding.signupButton.setOnClickListener(v -> {

            //Signup
            auth.createUserWithEmailAndPassword(
                    Objects.requireNonNull(signupBinding.usernameEdt.getText()).toString(),
                    Objects.requireNonNull(signupBinding.passwordEdt.getText()).toString()).addOnCompleteListener(task -> {

                        if (task.isSuccessful()){

                            HelpingPerson personNeed = new HelpingPerson();

                            if (!signupBinding.usernameEdt.getText().toString().isEmpty())

                                personNeed.setEmail(signupBinding.usernameEdt.getText().toString());

                            else
                                signupBinding.usernameEdt.setError("Please enter email");


                            if (!signupBinding.fullnameEdt.getText().toString().isEmpty())

                                personNeed.setUsername(signupBinding.fullnameEdt.getText().toString());

                            else
                                signupBinding.fullnameEdt.setError("Please enter name");


                            if (signupBinding.passwordEdt.getText().toString().equals(signupBinding.confirmPasswordEdt.getText().toString())
                                    && !signupBinding.passwordEdt.getText().toString().isEmpty() && !signupBinding.confirmPasswordEdt.getText().toString().isEmpty())

                                personNeed.setPassword(signupBinding.passwordEdt.getText().toString());
                            else
                                signupBinding.passwordEdt.setError("Passwords not match!");


                            personNeed.setId(String.valueOf(Long.MAX_VALUE - System.currentTimeMillis()));


                            preferenceManager.storeuser(personNeed);

                            //realtime database
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Helping_person")
                                    .child(personNeed.getId())
                                    .setValue(personNeed);

                            Intent intent = new Intent(SignupActivity.this, HomeWantActivity.class);
                            startActivity(intent);
                            finish();

                        }else{

                            Toast.makeText(SignupActivity.this,"Registration Fail",Toast.LENGTH_SHORT).show();
                        }
                    });


        });

        signupBinding.signinLink.setOnClickListener(v -> {

            Intent signinWant = new Intent(SignupActivity.this, WantLoginActivity.class);
            startActivity(signinWant);
            finish();

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}