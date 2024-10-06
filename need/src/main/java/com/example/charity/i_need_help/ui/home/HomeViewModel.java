package com.example.charity.i_need_help.ui.home;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.charity.Model.BillPayment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<ArrayList<BillPayment>> bill;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        bill = new MutableLiveData<>();

    }

    public MutableLiveData<ArrayList<BillPayment>> getListBill() {
        return bill;
    }


    public void getLiveDataFirebase(){

        ArrayList<BillPayment> billPayments = new ArrayList<>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Bill");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot billsnapshot: snapshot.getChildren()){

                    BillPayment billPayment = billsnapshot.getValue(BillPayment.class);

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