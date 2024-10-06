package com.example.ultimatebook.authentication.login.forgetpassword;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charity.databinding.ForgetPasswordBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Forget_password extends AppCompatActivity {

    private FirebaseAuth auth;
    private ForgetPasswordBinding passwordBinding;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        passwordBinding = ForgetPasswordBinding.inflate(getLayoutInflater());

        setContentView(passwordBinding.getRoot());

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(com.example.charity.R.color.start_button_color));

        auth = FirebaseAuth.getInstance();


        passwordBinding.continueButtonPhone.setOnClickListener(v -> auth.sendPasswordResetEmail(
                Objects.requireNonNull(
                        Objects.requireNonNull(passwordBinding.emailAddress).getText()).toString())
                .addOnSuccessListener(unused -> {

                    //Snack bar
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(parentLayout, "Reset link sent to you registered email", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Close", view -> snackbar.dismiss());
                    snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                    snackbar.show();

                    Toast.makeText(Forget_password.this, "Sent reset link!", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {

                    //Snack bar
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(parentLayout, "Unable to send reset link to email", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Close", view -> snackbar.dismiss());
                    snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                    snackbar.show();

                    Toast.makeText(Forget_password.this, "Unable to sent link! " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }));


    }
}