package com.example.ultimatebook.i_want.ui.home;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.charity.i_need_help.ui.home.HomeNeedActivity;
import com.example.charity.multi_language.BottomSheetFragment;
import com.example.ultimatebook.about.AboutActivity;
import com.example.ultimatebook.databinding.ActivityHomeWantBinding;
import com.example.ultimatebook.i_want.ui.home.adapter.BillAdapter;
import com.example.ultimatebook.preferences.PreferenceManager;

import java.util.ArrayList;
import java.util.Locale;

public class HomeWantActivity extends AppCompatActivity {

    private ActivityHomeWantBinding binding;
    private BillAdapter billAdapter;
    private PreferenceManager preferenceManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NotifyDataSetChanged")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeWantBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(com.example.charity.R.color.start_button_color));

        preferenceManager = new PreferenceManager(this);

        homeViewModel.getLiveDataFirebase();

        if (homeViewModel.isLoading_bill())

            binding.billContentLoader.setVisibility(View.VISIBLE);
        else
            binding.billContentLoader.setVisibility(View.GONE);

        homeViewModel.getListBill().observe(this, billPayments -> {

            if (billPayments != null) {

                billAdapter.setlist(billPayments);
                billAdapter.notifyDataSetChanged();
                binding.billContentLoader.setVisibility(View.GONE);



            } else {
                Toast.makeText(this, "Check internet connection!", Toast.LENGTH_SHORT).show();
            }

        });


        if (billAdapter == null)
            billAdapter = new BillAdapter(this);

        binding.needsToBeFilled.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.needsToBeFilled.setAdapter(billAdapter);


        homeViewModel.getLiveDataFirebase();

        if (homeViewModel.isLoading_bill())

            binding.billContentLoader.setVisibility(View.VISIBLE);
        else
            binding.billContentLoader.setVisibility(View.GONE);

        homeViewModel.getListBill().observe(this, billPayments -> {

            billAdapter.setlist(billPayments);
            billAdapter.notifyDataSetChanged();
            binding.billContentLoader.setVisibility(View.GONE);

        });

        try {

            if (preferenceManager.getLangauge().equals("en"))
                binding.switchLanguage.setText(getResources().getString(com.example.charity.R.string.english));
            else if (preferenceManager.getLangauge().equals("ur"))
                binding.switchLanguage.setText(getResources().getString(com.example.charity.R.string.urdu));
            else if (preferenceManager.getLangauge().equals("ar"))
                binding.switchLanguage.setText(getResources().getString(com.example.charity.R.string.arabic));
            else if (preferenceManager.getLangauge().equals("hi"))
                binding.switchLanguage.setText(getResources().getString(com.example.charity.R.string.hindi));

            setLocale();

        } catch (Exception e) {

            Log.d("Language Issue", e.getLocalizedMessage());
        }

        binding.switchLanguage.setOnClickListener(v -> {

            BottomSheetFragment language_fragment = BottomSheetFragment.newInstance();

            ArrayList<String> language = new ArrayList<>();
            language.add(getResources().getString(com.example.charity.R.string.english));
            language.add("اردو");
            language.add("عربي");
            language.add("हिन्दी");


            language_fragment.setLangauge(language);
            language_fragment.show(this.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

        });


        binding.swipeRefresh.setOnRefreshListener(() -> {

            homeViewModel.getLiveDataFirebase();

            if (homeViewModel.isLoading_bill())

                binding.billContentLoader.setVisibility(View.VISIBLE);
            else
                binding.billContentLoader.setVisibility(View.GONE);

            homeViewModel.getListBill().observe(this, billPayments -> {

                billAdapter.setlist(billPayments);
                billAdapter.notifyDataSetChanged();
                binding.billContentLoader.setVisibility(View.GONE);

            });

            binding.swipeRefresh.setRefreshing(false);
        });


        binding.about.setOnClickListener(v -> {

            Intent about = new Intent(HomeWantActivity.this, AboutActivity.class);
            startActivity(about);
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
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