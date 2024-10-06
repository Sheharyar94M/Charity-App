package com.example.haqddarcherity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.charity.i_need_help.ui.home.HomeNeedActivity;
import com.example.charity.multi_language.BottomSheetFragment;
import com.example.charity.preferences.PreferenceManager;
import com.example.haqddarcherity.about.AboutActivity;
import com.example.haqddarcherity.databinding.ActivityMainBinding;
import com.example.ultimatebook.i_want.ui.home.HomeWantActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private ActivityMainBinding mainBinding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(this);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.start_button_color));

        setContentView(mainBinding.getRoot());

        try {

            if (preferenceManager.getLangauge().equals("en"))
                mainBinding.switchLanguage.setText(getResources().getString(com.example.charity.R.string.english));
            else if (preferenceManager.getLangauge().equals("ur"))
                mainBinding.switchLanguage.setText(getResources().getString(com.example.charity.R.string.urdu));
            else if (preferenceManager.getLangauge().equals("ar"))
                mainBinding.switchLanguage.setText(getResources().getString(com.example.charity.R.string.arabic));
            else if (preferenceManager.getLangauge().equals("hi"))
                mainBinding.switchLanguage.setText(getResources().getString(com.example.charity.R.string.hindi));

            setLocale();

        } catch (Exception e) {

            Log.d("Language Issue", e.getLocalizedMessage());
        }

        mainBinding.switchLanguage.setOnClickListener(v -> {

            BottomSheetFragment language_fragment = BottomSheetFragment.newInstance();

            ArrayList<String> language = new ArrayList<>();
            language.add(getResources().getString(com.example.charity.R.string.english));
            language.add("اردو");
            language.add("عربي");
            language.add("हिन्दी");


            language_fragment.setLangauge(language);
            language_fragment.show(this.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

        });


        AppCompatButton need_help = findViewById(R.id.need_help);
        AppCompatButton want_help = findViewById(R.id.want_help);


        need_help.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HomeNeedActivity.class);
            startActivity(intent);
        });
        want_help.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this, HomeWantActivity.class);
            startActivity(intent1);
        });

        mainBinding.about.setOnClickListener(v -> {

            Intent about = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(about);

        });

    }

    public void setLocale(){

        Locale locale = new Locale(preferenceManager.getLangauge());
        Locale.setDefault(locale);

        Configuration localeConfig = new Configuration();

        localeConfig.locale = locale;

        getBaseContext().getResources().updateConfiguration(
                localeConfig, getBaseContext().getResources().getDisplayMetrics());
    }
}