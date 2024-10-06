package com.example.ultimatebook.i_want.ui.home;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ultimatebook.model.BillTobePaid;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<ArrayList<BillTobePaid>> bill;
    private boolean loading_bill = false;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        bill = new MutableLiveData<>();

    }

    public MutableLiveData<ArrayList<BillTobePaid>> getListBill() {
        return bill;
    }

    public boolean isLoading_bill() {
        return loading_bill;
    }

    public void getLiveDataFirebase(){

        loading_bill = true;

        ArrayList<BillTobePaid> billPayments = new ArrayList<>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Bill");

        db.keepSynced(true);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot billsnapshot: snapshot.getChildren()){

                    BillTobePaid billPayment = billsnapshot.getValue(BillTobePaid.class);

                    if (billPayment != null){

                        billPayments.add(billPayment);
                    }
                }

                bill.setValue(billPayments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplication().getApplicationContext(), "Unable to connect to data store!", Toast.LENGTH_SHORT).show();

            }
        });

    }
}