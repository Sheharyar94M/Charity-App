package com.example.charity.i_need_help.ui.home;

import static androidx.test.InstrumentationRegistry.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.charity.Model.BillPayment;
import com.example.charity.R;
import com.example.charity.about.AboutActivity;
import com.example.charity.authentication.login.LoginActivity;
import com.example.charity.cloud_notifications.MyNotificationSender;
import com.example.charity.databinding.ActivityHomeBinding;
import com.example.charity.i_need_help.ui.home.adapter.BillAdapter;
import com.example.charity.i_need_help.ui.home.dialog.CustomWarningDialogClass;
import com.example.charity.i_need_help.ui.verification_bill.VerificationActivity;
import com.example.charity.multi_language.BottomSheetFragment;
import com.example.charity.preferences.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class HomeNeedActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private String reference;
    private String due_date;
    private String amount;
    private String provider_name;
    private BillAdapter billAdapter;
    private HomeViewModel homeViewModel;
    private PreferenceManager preferenceManager;
    private String link_verifiction;
    private boolean verified=false;
//    private FirebaseUser user;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"NotifyDataSetChanged", "ResourceType"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.start_button_color));

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        preferenceManager = new PreferenceManager(this);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

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


        BillPayment bill = new BillPayment();

//        homeViewModel.getLiveDataFirebase();


//        if (getIntent() != null) {
//
//            user = (FirebaseUser) getIntent().getSerializableExtra("firebase_user");
//        }


//        homeViewModel.getListBill().observe(this, billPayments -> {
//
//            if (billPayments != null) {
//
//                billAdapter.setlist(billPayments);
//                billAdapter.notifyDataSetChanged();
//
//
//            } else {
//                Toast.makeText(this, "Unable to fetch Data! (Services)", Toast.LENGTH_SHORT).show();
//            }
//
//        });

        reference = Objects.requireNonNull(binding.reference.getText()).toString();
        due_date = Objects.requireNonNull(binding.dateDue.getText()).toString();
        amount = Objects.requireNonNull(binding.amount.getText()).toString();
        link_verifiction = Objects.requireNonNull(binding.linkVer.getText()).toString();


//        if (billAdapter == null)
//            billAdapter = new BillAdapter();
//
//        binding.postedjoblist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        binding.postedjoblist.setAdapter(billAdapter);


        //included layout button
        binding.submitButton.setOnClickListener(v1 -> {


            if (!binding.reference.getText().toString().isEmpty())
                reference = Objects.requireNonNull(binding.reference.getText()).toString();
            else
                binding.reference.setError("Please Fill reference no");

            if (!binding.dateDue.getText().toString().isEmpty())
                due_date = Objects.requireNonNull(binding.dateDue.getText()).toString();
            else
                binding.dateDue.setError("Please Fill due date");

            if (!binding.amount.getText().toString().isEmpty())
                if (binding.amount.getText().toString().length() <= 4)
                    amount = Objects.requireNonNull(binding.amount.getText()).toString();
                else
                    binding.amount.setError("You can not add bill more than 4 digits");
            else
                binding.amount.setError("Please Fill amount");

            if (binding.reference.getText().toString().length() > 14
                    || binding.reference.getText().toString().length() < 14) {

                binding.reference.setError("Invalid Reference Number!");

            }

            if (!binding.linkVer.getText().toString().isEmpty())
                link_verifiction = Objects.requireNonNull(binding.linkVer.getText()).toString();
            else
                binding.linkVer.setError("Please enter link verification");


            if (!binding.reference.getText().toString().isEmpty()
                    && !binding.dateDue.getText().toString().isEmpty()
                    && !binding.amount.getText().toString().isEmpty()
                    && !binding.linkVer.getText().toString().isEmpty()) {

                if (binding.lesco.isChecked())
                    bill.setProvider_name(binding.lesco.getText().toString());
                else {

                    binding.lesco.setChecked(false);

                    //Snack bar
                    final Snackbar snackBar = Snackbar.make(binding.getRoot().findViewById(android.R.id.content), "Please select provider", Snackbar.LENGTH_LONG);

                    snackBar.setAction("Close", view -> {
                        // Call your action method here
                        snackBar.dismiss();
                    });
                    snackBar.show();
                }

                if (binding.Isco.isChecked())
                    bill.setProvider_name(binding.Isco.getText().toString());
                else {

                    binding.hesco.setChecked(false);

                    //Snack bar
                    final Snackbar snackBar = Snackbar.make(binding.getRoot().findViewById(android.R.id.content), "Please select provider", Snackbar.LENGTH_LONG);

                    snackBar.setAction("Close", view -> {
                        // Call your action method here
                        snackBar.dismiss();
                    });
                    snackBar.show();
                }

                if (binding.hesco.isChecked())
                    bill.setProvider_name(binding.hesco.getText().toString());
                else {

                    binding.hesco.setChecked(false);

                    //Snack bar
                    final Snackbar snackBar = Snackbar.make(binding.getRoot().findViewById(android.R.id.content), "Please select provider", Snackbar.LENGTH_LONG);

                    snackBar.setAction("Close", view -> {
                        // Call your action method here
                        snackBar.dismiss();
                    });
                    snackBar.show();
                }

                if (binding.kepco.isChecked())
                    bill.setProvider_name(binding.kepco.getText().toString());
                else {

                    binding.kepco.setChecked(false);

                    //Snack bar
                    final Snackbar snackBar = Snackbar.make(binding.getRoot().findViewById(android.R.id.content), "Please select provider", Snackbar.LENGTH_LONG);

                    snackBar.setAction("Close", view -> {
                        // Call your action method here
                        snackBar.dismiss();
                    });
                    snackBar.show();
                }

                if (binding.gepco.isChecked())
                    bill.setProvider_name(binding.gepco.getText().toString());
                else {

                    binding.gepco.setChecked(false);

                    //Snack bar
                    final Snackbar snackBar = Snackbar.make(binding.getRoot().findViewById(android.R.id.content), "Please select provider", Snackbar.LENGTH_LONG);

                    snackBar.setAction("Close", view -> {
                        // Call your action method here
                        snackBar.dismiss();
                    });
                    snackBar.show();
                }

                bill.setReference_no(reference);
                bill.setDue_date(due_date);
                bill.setTotal_amount(amount);
                bill.setDescription(link_verifiction);
                bill.setId(String.valueOf(Long.MAX_VALUE - System.currentTimeMillis()));


                if (preferenceManager.isLoggedIn()) {

                    if (verified){

                        //realtime database
                        FirebaseDatabase.getInstance().getReference()
                                .child("Bill")
                                .child(bill.getId())
                                .setValue(bill);

                        //Snack bar
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(parentLayout, "you will be notified when your bill will be paid!", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Close", view -> {

                            snackbar.dismiss();
                        });
                        snackbar.setActionTextColor(getResources().getColor(R.color.white));
                        snackbar.show();


                        binding.reference.setText("");
                        binding.linkVer.setText("");
                        binding.amount.setText("");
                        binding.dateDue.setText("");
                        binding.gepco.setChecked(false);
                        binding.lesco.setChecked(false);
                        binding.hesco.setChecked(false);
                        binding.kepco.setChecked(false);
//                        binding.includeLayoutV.providerName.setText("");

                        homeViewModel.getLiveDataFirebase();

                        //sending notification
                        MyNotificationSender notificationSender = new MyNotificationSender(
                                "/topics/all",
                                "Help someone!",
                                "Someone needs help! tap to view the need to be fulfilled",
                                this, this);

                        //warning dialog
                        CustomWarningDialogClass warningDialogClass = new CustomWarningDialogClass(HomeNeedActivity.this);
                        warningDialogClass.show();

                        notificationSender.send_notification();
                    }else{

                        //Snack bar
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Please verify your bill first by clicking the verify below providers", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Close", view -> {

                            snackbar.dismiss();
                        });
                        snackbar.setActionTextColor(getResources().getColor(R.color.white));
                        snackbar.show();
                    }


                } else {

                    Intent signup = new Intent(this, LoginActivity.class);

                    signup.putExtra("bill_added", bill);
                    startActivity(signup);
                    finish();
                }

            }
        });

        binding.verifyButton.setOnClickListener(v -> {

            if (binding.lesco.isChecked()){

                verified = true;

                Intent verification_activity = new Intent(HomeNeedActivity.this, VerificationActivity.class);
                verification_activity.putExtra("provider",binding.lesco.getText().toString().trim());
                startActivity(verification_activity);

            }
            else if (binding.Isco.isChecked()){

                verified = true;

                Intent verification_activity = new Intent(HomeNeedActivity.this, VerificationActivity.class);
                verification_activity.putExtra("provider",binding.Isco.getText().toString().trim());
                startActivity(verification_activity);

            }
            else if (binding.gepco.isChecked()){

                verified = true;

                Intent verification_activity = new Intent(HomeNeedActivity.this, VerificationActivity.class);
                verification_activity.putExtra("provider",binding.gepco.getText().toString().trim());
                startActivity(verification_activity);

            }
            else if (binding.hesco.isChecked()){

                verified = true;

                Intent verification_activity = new Intent(HomeNeedActivity.this, VerificationActivity.class);
                verification_activity.putExtra("provider",binding.hesco.getText().toString().trim());
                startActivity(verification_activity);

            }
            else if (binding.kepco.isChecked()){

                verified = true;

                Intent verification_activity = new Intent(HomeNeedActivity.this, VerificationActivity.class);
                verification_activity.putExtra("provider",binding.kepco.getText().toString().trim());
                startActivity(verification_activity);

            }
            else{
                Toast.makeText(HomeNeedActivity.this, "Please select a provider first!", Toast.LENGTH_SHORT).show();
            }

        });

//        binding.cancelButton.setOnClickListener(v12 -> {
//
//            binding.includeLayout.setVisibility(View.GONE);
//            binding.gepco.setChecked(false);
//            binding.lesco.setChecked(false);
//            binding.hesco.setChecked(false);
//            binding.kepco.setChecked(false);
//
//
//        });

//        homeViewModel.getLiveDataFirebase();
//
//        homeViewModel.getListBill().observe(this, billPayments -> {
//
//            billAdapter.setlist(billPayments);
//
//        });


//        binding.imageBill.setOnClickListener(v -> {
//
//            CustomDialogClass dialogClass = new CustomDialogClass(requireActivity());
//            dialogClass.show();
//
//        });

        binding.about.setOnClickListener(v -> {

            Intent about = new Intent(HomeNeedActivity.this, AboutActivity.class);
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