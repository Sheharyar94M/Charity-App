package com.example.charity.i_need_help;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.charity.Model.BillPayment;
import com.example.charity.Model.PersonNeed;
import com.example.charity.preferences.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Objects;

public class AccountViewModel extends AndroidViewModel {

    private final MutableLiveData<PersonNeed> person;
    private FirebaseAuth auth;


    public AccountViewModel(@NonNull Application application) {
        super(application);
        person = new MutableLiveData<>();

    }

    public void fetch_data_firebase() {

        auth = FirebaseAuth.getInstance();

        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference userDb1 = userDB.child("Needy_person");

        userDb1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (Objects.equals(
                            dataSnapshot.child("email").getValue(String.class),
                            Objects.requireNonNull(auth.getCurrentUser()).getEmail())) {

                        person.setValue(dataSnapshot.getValue(PersonNeed.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public LiveData<PersonNeed> getLiveData() {
        return person;
    }

    public void update_user(PersonNeed user_updated){

        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("User");

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot level1: snapshot.getChildren()){

                    if (level1.exists()){
                        if (level1.child("id").equals(user_updated.getId())){

                            level1.getRef().child("account_name").setValue(user_updated.getUsername());
                            level1.getRef().child("contact").setValue(user_updated.getContact());
                            level1.getRef().child("password").setValue(user_updated.getPassword());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}