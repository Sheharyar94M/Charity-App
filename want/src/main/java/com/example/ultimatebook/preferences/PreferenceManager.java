package com.example.ultimatebook.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ultimatebook.model.HelpingPerson;


public class PreferenceManager {

    SharedPreferences usersession;
    SharedPreferences.Editor editor;
    Context context;

    private String IS_LOGGED_IN = "IsLoggedIn";
    private String MAIL = "Mail";
    private String ID = "ID";
    private String NAME = "Name";
    private String PHONE = "PHONE";
    private String PASSWORD = "PASSWORD";
    private String LANGUAGE = "LANGUAGE";

    public PreferenceManager(Context context){

        usersession = context.getSharedPreferences("user_session_want",Context.MODE_PRIVATE);
        editor = usersession.edit();

    }

    public void storeuser(HelpingPerson user){

        editor.putBoolean(IS_LOGGED_IN,true);
        editor.putString(MAIL,user.getEmail());
        editor.putString(ID,user.getId());
        editor.putString(NAME,user.getUsername());
        editor.putString(PASSWORD,user.getPassword());
        editor.commit();
    }

    public HelpingPerson getuser(){

        HelpingPerson user = new HelpingPerson();
        user.setEmail(usersession.getString(MAIL,null));
        user.setUsername(usersession.getString(NAME,null));
        user.setId(usersession.getString(ID,null));
        user.setPassword(usersession.getString(PASSWORD,null));

        return user;
    }

    public boolean isLoggedIn(){

        if (usersession.getBoolean(IS_LOGGED_IN,false)){
            return true;
        }else
            return false;
    }

    public void storeLanguage(String langauge){

        editor.putString(LANGUAGE,langauge);
        editor.commit();

    }

    public String getLangauge(){

        String language = usersession.getString(LANGUAGE,"en");

        return language;

    }

    public void logout(){

        editor.clear();
        editor.commit();
    }
}
