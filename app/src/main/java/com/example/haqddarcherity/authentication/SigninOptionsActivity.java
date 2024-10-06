package com.example.haqddarcherity.authentication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charity.authentication.login.LoginActivity;
import com.example.charity.authentication.sign_up.SignupActivity;
import com.example.haqddarcherity.MainActivity;
import com.example.haqddarcherity.R;
import com.example.haqddarcherity.about.AboutActivity;
import com.example.haqddarcherity.databinding.ActivitySigninOptionsBinding;

public class SigninOptionsActivity extends AppCompatActivity {

    private ActivitySigninOptionsBinding signinOptionsBinding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signinOptionsBinding = ActivitySigninOptionsBinding.inflate(getLayoutInflater());

        setContentView(signinOptionsBinding.getRoot());

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.start_button_color));

        signinOptionsBinding.wantHelpLogin.setOnClickListener(v -> {

            Intent LoginActivityWant = new Intent(SigninOptionsActivity.this, com.example.ultimatebook.authentication.login.WantLoginActivity.class);
            startActivity(LoginActivityWant);

        });

        signinOptionsBinding.signupWantHelp.setOnClickListener(v -> {

            Intent SignupActivityWant = new Intent(SigninOptionsActivity.this, com.example.ultimatebook.authentication.sign_up.SignupActivity.class);
            startActivity(SignupActivityWant);

        });

        signinOptionsBinding.needHelpLogin.setOnClickListener(v -> {

            Intent LoginActivityNeed = new Intent(SigninOptionsActivity.this, LoginActivity.class);
            startActivity(LoginActivityNeed);

        });

        signinOptionsBinding.signupNeedHelp.setOnClickListener(v -> {

            Intent SignupActivityNeed = new Intent(SigninOptionsActivity.this, SignupActivity.class);
            startActivity(SignupActivityNeed);

        });

        signinOptionsBinding.skip.setOnClickListener(v -> {

            Intent mainActivity = new Intent(SigninOptionsActivity.this, MainActivity.class);
            startActivity(mainActivity);

        });

        //about
        signinOptionsBinding.about.setOnClickListener(v -> {

            Intent about = new Intent(SigninOptionsActivity.this, AboutActivity.class);
            startActivity(about);

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}