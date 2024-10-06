package com.example.haqddarcherity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.example.haqddarcherity.authentication.SigninOptionsActivity;
import com.example.haqddarcherity.databinding.ActivitySplashBinding;
import com.example.ultimatebook.preferences.PreferenceManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding splashBinding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(splashBinding.getRoot());

        preferenceManager = new PreferenceManager(this);

//        Animation a = new RotateAnimation(0.0f, 90.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);



        splashBinding.imageView.animate().setDuration(3000).rotationYBy(360f).rotationY(360f).start();

        Thread thread = new Thread() {

            @Override
            public void run() {
                super.run();
                try {

                    sleep(2500);

                } catch (Exception e) {

                    e.printStackTrace();

                } finally {

                    if (preferenceManager.isLoggedIn()){


                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else{

                        Intent intent = new Intent(SplashActivity.this, SigninOptionsActivity.class);
                        startActivity(intent);
                        finish();

                    }

                }


            }
        };

        thread.start();
    }
}