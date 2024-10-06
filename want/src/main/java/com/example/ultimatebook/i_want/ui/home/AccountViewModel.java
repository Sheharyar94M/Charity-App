package com.example.ultimatebook.i_want.ui.home;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ultimatebook.model.HelpingPerson;
import com.example.ultimatebook.preferences.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AccountViewModel extends AndroidViewModel {

    private final MutableLiveData<HelpingPerson> person;
    private FirebaseAuth auth;


    public AccountViewModel(@NonNull Application application) {
        super(application);
        person = new MutableLiveData<>();

    }

    public LiveData<HelpingPerson> getLiveData() {
        return person;
    }

    public void getUserFromPreference(){

        PreferenceManager preferenceManager = new PreferenceManager(getApplication().getApplicationContext());

        person.setValue(preferenceManager.getuser());

    }

    public void fetch_data_firebase() {

        auth = FirebaseAuth.getInstance();

        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference userDb1 = userDB.child("Helping_person");

        userDb1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (Objects.equals(
                            dataSnapshot.child("email").getValue(String.class),
                            Objects.requireNonNull(auth.getCurrentUser()).getEmail())) {

                        person.setValue(dataSnapshot.getValue(HelpingPerson.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void update_user(HelpingPerson user_updated){

        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Helping_person");

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot level1: snapshot.getChildren()){

                    if (level1.exists()){
                        if (level1.child("id").equals(user_updated.getId())){

                            level1.getRef().child("account_name").setValue(user_updated.getUsername());
                            level1.getRef().child("password").setValue(user_updated.getPassword());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplication().getApplicationContext(), "Check internet connection!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}