package com.example.haqddarcherity.multi_language.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.charity.R;
import com.example.charity.preferences.PreferenceManager;

import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    private final ArrayList<String> langauge;
    private final LayoutInflater layoutInflater;
    private Activity activity;
    private PreferenceManager preferenceManager;
    private Context context;

    public ListViewAdapter(Activity activity, Context context, ArrayList<String> langauge) {
        this.langauge = langauge;
        this.activity = activity;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (langauge != null)
            return langauge.size();
        else
            return 0;    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    @SuppressLint({"ViewHolder","InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.language_list_layout, null);

        AppCompatTextView languageName = view.findViewById(R.id.language_name);

        preferenceManager = new PreferenceManager(context);

        languageName.setText(langauge.get(position));


        view.setOnClickListener(v -> {

            if (position == 0){

                setLocale("en");
                preferenceManager.storeLanguage("en");
                activity.recreate();

            }else if (position == 1){

                setLocale("ur");
                preferenceManager.storeLanguage("ur");
                activity.recreate();

            }else if (position == 2){

                setLocale("ar");
                preferenceManager.storeLanguage("ar");
                activity.recreate();

            }else if (position == 3){

                setLocale("hi");
                preferenceManager.storeLanguage("hi");
                activity.recreate();
            }

        });

        return view;
    }

    private void setLocale(String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration localeConfig = new Configuration();

        localeConfig.locale = locale;

        activity.getBaseContext().getResources().updateConfiguration(
                localeConfig, activity.getBaseContext().getResources().getDisplayMetrics());

    }
}
