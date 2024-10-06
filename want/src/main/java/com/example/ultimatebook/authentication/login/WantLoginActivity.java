package com.example.ultimatebook.authentication.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.charity.authentication.login.forgetpassword.Forget_password;
import com.example.ultimatebook.authentication.sign_up.SignupActivity;
import com.example.ultimatebook.cloud_notifications.MyFirebaseMessagingService;
import com.example.ultimatebook.cloud_notifications.MyNotificationSender;
import com.example.ultimatebook.databinding.ActivityWantLoginBinding;
import com.example.ultimatebook.i_want.ui.home.AccountViewModel;
import com.example.ultimatebook.i_want.ui.home.HomeWantActivity;
import com.example.ultimatebook.model.BillTobePaid;
import com.example.ultimatebook.model.HelpingPerson;
import com.example.ultimatebook.preferences.PreferenceManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;

import java.util.Collections;
import java.util.Objects;

public class WantLoginActivity extends AppCompatActivity {

    private ActivityWantLoginBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth auth;
    private String username;
    private String password;
    private BillTobePaid bill;
    private GoogleSignInClient gsc;
    private AccountViewModel accountViewModel;
    private boolean isLoggedIn = false;
    private HelpingPerson helpingPerson_already_account;

    //facebook variables
    private CallbackManager callbackManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWantLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(com.example.charity.R.color.start_button_color));


        FirebaseMessaging.getInstance().subscribeToTopic("all");


        callbackManager = CallbackManager.Factory.create();

        //facebook work
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        preferenceManager = new PreferenceManager(this);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);


        auth = FirebaseAuth.getInstance();


        //get objet to add
        if (getIntent() != null) {

            bill = (BillTobePaid) getIntent().getSerializableExtra("bill_added");
        }


        binding.registerLink.setOnClickListener(v -> {

            Intent signup = new Intent(WantLoginActivity.this, SignupActivity.class);
            startActivity(signup);
        });

        binding.forgotPassword.setOnClickListener(v -> {

            // set Fragment class Arguments
            Intent forget_password = new Intent(WantLoginActivity.this, Forget_password.class);
            startActivity(forget_password);

        });


        //Signin simple
        binding.login.setOnClickListener(v -> {

            if (!Objects.requireNonNull(binding.usernameEdt.getText()).toString().isEmpty())
                username = binding.usernameEdt.getText().toString();
            else
                binding.usernameEdt.setError("Please enter email");

            if (!Objects.requireNonNull(binding.passwordEdt.getText()).toString().isEmpty())
                password = binding.passwordEdt.getText().toString();
            else
                binding.passwordEdt.setError("Please enter password");

            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, task -> {

                if (task.isSuccessful()) {

                    if (bill != null) {

                        accountViewModel.fetch_data_firebase();

                        accountViewModel.getLiveData().observe(this, personNeed -> {

                            if (personNeed != null) {

                                preferenceManager.storeuser(personNeed);

                                //realtime database
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Bill")
                                        .child(bill.getId())
                                        .setValue(bill);

                                //Snack bar
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                                snackbar.setAction("Close", view -> snackbar.dismiss());
                                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                                snackbar.show();


                                //new activity
                                Intent needActivity = new Intent(WantLoginActivity.this, WantLoginActivity.class);
                                startActivity(needActivity);
                                finish();

                                //sending notification
                                MyNotificationSender notificationSender = new MyNotificationSender(
                                        "/topics/all",
                                        "Help me!",
                                        "Someone needs help! tap to view the need to be fulfilled",
                                        this,WantLoginActivity.this);

                                notificationSender.send_notification();

                                Toast.makeText(WantLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(WantLoginActivity.this, "you will be notified when your bill will be paid!", Toast.LENGTH_SHORT).show();

                                startService(new Intent(this, MyFirebaseMessagingService.class));

                            }

                        });

                    } else {

                        accountViewModel.fetch_data_firebase();

                        accountViewModel.getLiveData().observe(this, personNeed -> {

                            if (personNeed != null) {

                                preferenceManager.storeuser(personNeed);

                                //Snack bar
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                                snackbar.setAction("Close", view -> snackbar.dismiss());
                                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                                snackbar.show();

                                Toast.makeText(WantLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                //new activity
                                Intent needActivity = new Intent(WantLoginActivity.this, HomeWantActivity.class);
                                startActivity(needActivity);
                                finish();

                                startService(new Intent(this, MyFirebaseMessagingService.class));

                            }

                        });


                    }


                } else {

                    Toast.makeText(WantLoginActivity.this, "Account does not exists! Please try to sign up!", Toast.LENGTH_LONG).show();
                }
            });

        });

        //Login with google

        binding.signinWithGoogle.setOnClickListener(v -> signin());

        //facebook work
        binding.loginButtonFacebook.setOnClickListener(v ->

                LoginManager.getInstance().logInWithReadPermissions(WantLoginActivity.this, Collections.singletonList("public_profile"))

        );

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (!loginResult.getRecentlyGrantedPermissions().isEmpty()){

                            GraphRequest request = GraphRequest.newMeRequest(
                                    accessToken,
                                    (data, response) -> {

                                        try {
                                            //user data from facebook
                                            String email = Objects.requireNonNull(data).getString("email");
                                            String name = Objects.requireNonNull(data).getString("name");

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                            ref.keepSynced(true);

                                            ref.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        HelpingPerson personNeed = dataSnapshot.getValue(HelpingPerson.class);

                                                        try {

                                                            if (personNeed != null) {

                                                                if (personNeed.getEmail().equalsIgnoreCase(email)) {

                                                                    isLoggedIn = true;

                                                                    helpingPerson_already_account = personNeed;

                                                                } else {

                                                                    isLoggedIn = false;

                                                                }
                                                            } else {

                                                                isLoggedIn = false;

                                                            }
                                                        } catch (Exception error) {

                                                            Log.d("Null Database user", error.getLocalizedMessage());

                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                    throw error.toException();

                                                }
                                            });

                                            if (!isLoggedIn) {

                                                HelpingPerson helpingNeed = new HelpingPerson(name, email, null, String.valueOf(Long.MAX_VALUE - System.currentTimeMillis()));

                                                //realtime database
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Users")
                                                        .child("Needy_person")
                                                        .child(helpingNeed.getId())
                                                        .setValue(helpingNeed);

                                                preferenceManager.storeuser(helpingNeed);

                                                Log.d("signup_user", helpingNeed.toString());

                                                //Snack bar
                                                View parentLayout = findViewById(android.R.id.content);
                                                Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                                                snackbar.setAction("Close", view -> snackbar.dismiss());

                                                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                                                snackbar.show();

                                                //signin
                                                Intent login = new Intent(WantLoginActivity.this, HomeWantActivity.class);
                                                startActivity(login);
                                                finish();



                                            } else {

                                                preferenceManager.storeuser(helpingPerson_already_account);

                                                //Snack bar
                                                View parentLayout = findViewById(android.R.id.content);
                                                Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                                                snackbar.setAction("Close", view -> snackbar.dismiss());

                                                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                                                snackbar.show();


                                                //signin
                                                Intent login = new Intent(WantLoginActivity.this, HomeWantActivity.class);
                                                startActivity(login);
                                                finish();

                                            }

                                            if (bill != null) {
                                                //realtime database
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Bill")
                                                        .child(bill.getId())
                                                        .setValue(bill);

                                                Toast.makeText(WantLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(WantLoginActivity.this, "you will be notified when your bill will be paid!", Toast.LENGTH_SHORT).show();


                                                //sending notification
                                                MyNotificationSender notificationSender = new MyNotificationSender(
                                                        "/topics/all",
                                                        "Help me!",
                                                        "Someone needs help! tap to view the need to be fulfilled",
                                                        WantLoginActivity.this,WantLoginActivity.this);

                                                notificationSender.send_notification();

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,link,email");
                            request.setParameters(parameters);
                            request.executeAsync();

                        }
                    }

                    @Override
                    public void onCancel() {
                        //Snack bar
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Signin cancelled", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Close", view -> snackbar.dismiss());
                        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                        snackbar.show();
                    }

                    @Override
                    public void onError(@NonNull FacebookException exception) {

                        //Snack bar
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Unable to signin", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Close", view -> snackbar.dismiss());
                        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                        snackbar.show();


                    }
                });

    }

    public void signin() {

        Intent signinIntent = gsc.getSignInIntent();
        startActivityForResult(signinIntent, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        //Google
        if (requestCode == 1000) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);

                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

                if (account != null) {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                    ref.keepSynced(true);

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                HelpingPerson personNeed = dataSnapshot.getValue(HelpingPerson.class);

                                try {

                                    if (personNeed != null) {

                                        if (personNeed.getEmail().equalsIgnoreCase(account.getEmail())) {

                                            isLoggedIn = true;

                                            helpingPerson_already_account = personNeed;

                                        } else {

                                            isLoggedIn = false;

                                        }
                                    } else {

                                        isLoggedIn = false;

                                    }
                                } catch (Exception error) {

                                    Log.d("Null Database user", error.getLocalizedMessage());

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            throw error.toException();

                        }
                    });

                    if (!isLoggedIn) {

                        HelpingPerson personNeed = new HelpingPerson(account.getDisplayName(), account.getEmail(), null, String.valueOf(Long.MAX_VALUE - System.currentTimeMillis()));

                        //realtime database
                        FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child("Needy_person")
                                .child(personNeed.getId())
                                .setValue(personNeed);

                        preferenceManager.storeuser(personNeed);

                        Log.d("signup_user", personNeed.toString());

                        //Snack bar
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Close", view -> snackbar.dismiss());

                        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                        snackbar.show();

                        //signin
                        Intent login = new Intent(WantLoginActivity.this, HomeWantActivity.class);
                        startActivity(login);
                        finish();

                    } else {

                        preferenceManager.storeuser(helpingPerson_already_account);

                        //Snack bar
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Close", view -> snackbar.dismiss());

                        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                        snackbar.show();


                        //signin
                        Intent login = new Intent(WantLoginActivity.this, HomeWantActivity.class);
                        startActivity(login);
                        finish();

                    }

                    if (bill != null) {
                        //realtime database
                        FirebaseDatabase.getInstance().getReference()
                                .child("Bill")
                                .child(bill.getId())
                                .setValue(bill);

                        Toast.makeText(WantLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(WantLoginActivity.this, "you will be notified when your bill will be paid!", Toast.LENGTH_SHORT).show();

                        //sending notification
                        MyNotificationSender notificationSender = new MyNotificationSender(
                                "/topics/all",
                                "Help me!",
                                "Someone needs help! tap to view the need to be fulfilled",
                                WantLoginActivity.this,WantLoginActivity.this);

                        notificationSender.send_notification();

                    }
                } else {

                    //Snack bar
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(parentLayout, "Invalid account!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Close", view -> snackbar.dismiss());
                    snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                    snackbar.show();
                }


            } catch (ApiException e) {

                //Snack bar
                View parentLayout = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(parentLayout, "Unable to signin", Snackbar.LENGTH_LONG);
                snackbar.setAction("Close", view -> snackbar.dismiss());
                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                snackbar.show();

            }
        } else {

            //Snack bar
            View parentLayout = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentLayout, "Bad Request", Snackbar.LENGTH_LONG);
            snackbar.setAction("Close", view -> snackbar.dismiss());
            snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
            snackbar.show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}