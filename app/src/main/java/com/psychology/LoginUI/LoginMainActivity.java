package com.psychology.LoginUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.psychology.MainActivity;
import com.psychology.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class LoginMainActivity extends AppCompatActivity {

    Button getOtp;
    EditText mobileNumber;

    String codeSent;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final static int RC_SIGN_IN = 2;
    GoogleSignInClient mGoogleSignInClient;

    ProgressBar progressBar;

    SignInButton google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        getOtp = findViewById(R.id.get_otp_button);
        google = findViewById(R.id.google_button);
        mobileNumber = findViewById(R.id.login_phone_edit);
        progressBar = findViewById(R.id.progressBar);

        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mobileNumber.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
                }
                else{
                   sendOTP();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Sign in failed",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sign in failed",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Sign in complete",Toast.LENGTH_SHORT).show();
                            redirect();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void sendOTP(){
        progressBar.setVisibility(View.VISIBLE);
        String phoneNumber = mobileNumber.getText().toString();

        String number = "+" + String.valueOf(GetCountryZipCode()) + phoneNumber;

        Log.i("code Country", String.valueOf(number));

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginMainActivity.this,"Please enter correct phone number", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;

            Intent intent = new Intent(LoginMainActivity.this,LoginOtpActivity.class);
            intent.putExtra("otp",s);
            intent.putExtra("phone", mobileNumber.getText().toString());
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(intent);
            finish();
        }

    };

    public String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    private void redirect(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), LoginMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}