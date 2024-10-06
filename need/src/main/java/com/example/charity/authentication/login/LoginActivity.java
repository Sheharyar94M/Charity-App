package com.example.charity.authentication.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.example.charity.Model.BillPayment;
import com.example.charity.Model.PersonNeed;
import com.example.charity.R;
import com.example.charity.authentication.login.forgetpassword.Forget_password;
import com.example.charity.authentication.sign_up.SignupActivity;
import com.example.charity.cloud_notifications.MyFirebaseMessagingService;
import com.example.charity.cloud_notifications.MyNotificationSender;
import com.example.charity.databinding.ActivityLoginBinding;
import com.example.charity.i_need_help.AccountViewModel;
import com.example.charity.i_need_help.ui.home.HomeNeedActivity;
import com.example.charity.preferences.PreferenceManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
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

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth auth;
    private String username;
    private String password;
    private BillPayment bill;
    private PersonNeed personNeed;
    private GoogleSignInClient gsc;
    private AccountViewModel accountViewModel;
    private boolean isLoggedIn = false;
    private PersonNeed personNeed_already_account;

    //facebook variables
    private CallbackManager callbackManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.start_button_color));

        FirebaseMessaging.getInstance().subscribeToTopic("all");


        callbackManager = CallbackManager.Factory.create();
//
//        //facebook work
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(getApplication());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        preferenceManager = new PreferenceManager(this);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);


        auth = FirebaseAuth.getInstance();


        //get objet to add bill
        if (getIntent() != null) {

            bill = (BillPayment) getIntent().getSerializableExtra("bill_added");
        }


        binding.registerLink.setOnClickListener(v -> {

            Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signup);
        });

        binding.forgotPassword.setOnClickListener(v -> {

            // set Fragment class Arguments

            binding.layout1.setVisibility(View.GONE);

            Intent forget_password = new Intent(LoginActivity.this, Forget_password.class);
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
                                preferenceManager.storeLoginStatus();

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
                                Intent needActivity = new Intent(LoginActivity.this, HomeNeedActivity.class);
                                startActivity(needActivity);
                                finish();

                                //sending notification
                                MyNotificationSender notificationSender = new MyNotificationSender(
                                        "/topics/all",
                                        "Help someone!",
                                        "Someone needs help! tap to view the need to be fulfilled",
                                        this,LoginActivity.this);

                                notificationSender.send_notification();

                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "you will be notified when your bill will be paid!", Toast.LENGTH_SHORT).show();

                            }

                        });

                    } else {

                        accountViewModel.fetch_data_firebase();

                        accountViewModel.getLiveData().observe(this, personNeed -> {

                            if (personNeed != null) {

                                preferenceManager.storeuser(personNeed);
                                preferenceManager.storeLoginStatus();

                                //Snack bar
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                                snackbar.setAction("Close", view -> snackbar.dismiss());
                                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                                snackbar.show();

                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                //new activity
                                Intent needActivity = new Intent(LoginActivity.this, HomeNeedActivity.class);
                                startActivity(needActivity);
                                finish();

                                startService(new Intent(this, MyFirebaseMessagingService.class));

                            }

                        });


                    }


                } else {

                    Toast.makeText(LoginActivity.this, "Account does not exists! Please try to sign up!", Toast.LENGTH_LONG).show();
                }
            });

        });

        //Login with google

        binding.signinWithGoogle.setOnClickListener(v -> signin());

        //facebook work
        binding.loginButtonFacebook.setOnClickListener(v ->

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Collections.singletonList("public_profile"))

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

                                                        PersonNeed personNeed = dataSnapshot.getValue(PersonNeed.class);

                                                        try {

                                                            if (personNeed != null) {

                                                                if (personNeed.getEmail().equalsIgnoreCase(email)) {

                                                                    isLoggedIn = true;

                                                                    personNeed_already_account = personNeed;

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

                                                PersonNeed personNeed = new PersonNeed(name, email, null, null, String.valueOf(Long.MAX_VALUE - System.currentTimeMillis()));

                                                //realtime database
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Users")
                                                        .child("Needy_person")
                                                        .child(personNeed.getId())
                                                        .setValue(personNeed);

                                                preferenceManager.storeuser(personNeed);
                                                preferenceManager.storeLoginStatus();

                                                Log.d("signup_user", personNeed.toString());

                                                //Snack bar
                                                View parentLayout = findViewById(android.R.id.content);
                                                Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                                                snackbar.setAction("Close", view -> snackbar.dismiss());

                                                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                                                snackbar.show();

                                                //signin
                                                Intent login = new Intent(LoginActivity.this, HomeNeedActivity.class);
                                                startActivity(login);
                                                finish();



                                            } else {

                                                preferenceManager.storeuser(personNeed_already_account);
                                                preferenceManager.storeLoginStatus();

                                                //Snack bar
                                                View parentLayout = findViewById(android.R.id.content);
                                                Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                                                snackbar.setAction("Close", view -> snackbar.dismiss());

                                                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                                                snackbar.show();


                                                //signin
                                                Intent login = new Intent(LoginActivity.this, HomeNeedActivity.class);
                                                startActivity(login);
                                                finish();

                                            }

                                            if (bill != null) {
                                                //realtime database
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Bill")
                                                        .child(bill.getId())
                                                        .setValue(bill);

                                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(LoginActivity.this, "you will be notified when your bill will be paid!", Toast.LENGTH_SHORT).show();


                                                //sending notification
                                                MyNotificationSender notificationSender = new MyNotificationSender(
                                                        "/topics/all",
                                                        "Help someone!",
                                                        "Someone needs help! tap to view the need to be fulfilled",
                                                        LoginActivity.this,LoginActivity.this);

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

        //show hide password
        assert binding.hidePasswordLogin != null;
        binding.hidePasswordLogin.setOnClickListener(this::ShowHidePass);

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

                                PersonNeed personNeed = dataSnapshot.getValue(PersonNeed.class);

                                try {

                                    if (personNeed != null) {

                                        if (personNeed.getEmail().equalsIgnoreCase(account.getEmail())) {

                                            isLoggedIn = true;

                                            personNeed_already_account = personNeed;

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

                        PersonNeed personNeed = new PersonNeed(account.getDisplayName(), account.getEmail(), null, null, String.valueOf(Long.MAX_VALUE - System.currentTimeMillis()));

                        //realtime database
                        FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child("Needy_person")
                                .child(personNeed.getId())
                                .setValue(personNeed);

                        preferenceManager.storeuser(personNeed);
                        preferenceManager.storeLoginStatus();

                        Log.d("signup_user", personNeed.toString());

                        //Snack bar
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Close", view -> snackbar.dismiss());

                        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                        snackbar.show();

                        //signin
                        Intent login = new Intent(LoginActivity.this, HomeNeedActivity.class);
                        startActivity(login);
                        finish();

                    } else {

                        preferenceManager.storeuser(personNeed_already_account);
                        preferenceManager.storeLoginStatus();

                        //Snack bar
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Login Successful", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Close", view -> snackbar.dismiss());

                        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                        snackbar.show();


                        //signin
                        Intent login = new Intent(LoginActivity.this, HomeNeedActivity.class);
                        startActivity(login);
                        finish();

                    }

                    if (bill != null) {
                        //realtime database
                        FirebaseDatabase.getInstance().getReference()
                                .child("Bill")
                                .child(bill.getId())
                                .setValue(bill);

                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "you will be notified when your bill will be paid!", Toast.LENGTH_SHORT).show();

                        //sending notification
                        MyNotificationSender notificationSender = new MyNotificationSender(
                                "/topics/all",
                                "Help someone!",
                                "Someone needs help! tap to view the need to be fulfilled",
                                LoginActivity.this,LoginActivity.this);

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

    public void ShowHidePass(View view){

        if(view.getId()==R.id.hide_password_login){

            if(binding.passwordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                assert binding.hidePasswordLogin != null;
                binding.hidePasswordLogin.setImageResource(R.drawable.hide_password);

                //Show Password
                binding.passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                assert binding.hidePasswordLogin != null;
                binding.hidePasswordLogin.setImageResource(R.drawable.show_password);

                //Hide Password
                binding.passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}