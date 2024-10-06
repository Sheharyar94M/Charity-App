package com.example.charity.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.charity.Model.PersonNeed;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class PreferenceManager {

    SharedPreferences usersession;
    SharedPreferences.Editor editor;

    private String IS_LOGGED_IN = "IsLoggedIn";
    private String MAIL = "Mail";
    private String ID = "ID";
    private String NAME = "Name";
    private String PHONE = "PHONE";
    private String PASSWORD = "PASSWORD";
    private String LANGUAGE = "LANGUAGE";

    public PreferenceManager(Context context){

        usersession = context.getApplicationContext().getSharedPreferences("user_session",Context.MODE_PRIVATE);
        editor = usersession.edit();

    }

    public void storeuser(PersonNeed user){

        editor.putString(MAIL,user.getEmail());
        editor.putString(ID,user.getId());
        editor.putString(NAME,user.getUsername());
        editor.putString(PHONE,user.getContact());
        editor.putString(PASSWORD,user.getPassword());
        editor.commit();
    }

    public void storeLoginStatus(){

        editor.putBoolean(IS_LOGGED_IN,true);
        editor.commit();
    }


    public PersonNeed getuser(){

        PersonNeed user = new PersonNeed();
        user.setEmail(usersession.getString(MAIL,null));
        user.setUsername(usersession.getString(NAME,null));
        user.setId(usersession.getString(ID,null));
        user.setContact(usersession.getString(PHONE,null));
        user.setPassword(usersession.getString(PASSWORD,null));

        return user;
    }

    public void storeLanguage(String langauge){

        editor.putString(LANGUAGE,langauge);
        editor.commit();

    }

    public String getLangauge(){

        String language = usersession.getString(LANGUAGE,"en");

        return language;

    }

    public boolean isLoggedIn(){

        if (usersession.getBoolean(IS_LOGGED_IN,false)){
            return true;
        }else
            return false;
    }

    public void logout(){

        editor.clear();
        editor.commit();
    }
}
