package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    private Button verify,sendOTP;
    private EditText mobile,Pass;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private TextView format;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance ();

//user is already logged in
        FirebaseUser user = mAuth.getCurrentUser ();
        if(user != null)
        {
            finish ();
            startActivity (new Intent (Login.this,profileActivity.class));
        }

        verify = (Button)findViewById (R.id.Login);
          sendOTP = (Button)findViewById (R.id.otp);
        mobile = (EditText)findViewById (R.id.phone);
        Pass = (EditText)findViewById (R.id.verify);
        loadingbar = new ProgressDialog (this);
        format = (TextView)findViewById (R.id.format);


        sendOTP.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                String number = mobile.getText ().toString ().trim();

                if(TextUtils.isEmpty (number))
                {
                    Toast.makeText (Login.this, "Please enter your phone number", Toast.LENGTH_SHORT).show ();
                }else{

                    loadingbar.setTitle ("Phone Verification");
                    loadingbar.setMessage ("Please wait, while we authenticate your number");
                    loadingbar.setCanceledOnTouchOutside (false);
                    loadingbar.show ();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            number,        // Phone number to verify
                            120,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            Login.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });


        verify.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                sendOTP.setVisibility (View.INVISIBLE);
                mobile.setVisibility (View.INVISIBLE);
                format.setVisibility (View.INVISIBLE);

                String OneTimePassword = Pass.getText ().toString ();

                if(TextUtils.isEmpty (OneTimePassword))
                {
                    Toast.makeText (Login.this, "Please enter verification code first", Toast.LENGTH_SHORT).show ();
                }else{

                    loadingbar.setTitle ("OTP Verification");
                    loadingbar.setMessage ("Please wait, while we authenticate OTP");
                    loadingbar.setCanceledOnTouchOutside (false);
                    loadingbar.show ();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, OneTimePassword);
                    signInWithPhoneAuthCredential (credential);
                }

            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks () {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingbar.dismiss ();

                Toast.makeText (Login.this, "Invalid Mobile Number ", Toast.LENGTH_SHORT).show ();
                sendOTP.setVisibility (View.VISIBLE);
                mobile.setVisibility (View.VISIBLE);
                format.setVisibility (View.VISIBLE);

                Pass.setVisibility (View.INVISIBLE);
                verify.setVisibility (View.INVISIBLE);
            }

            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                loadingbar.dismiss ();
                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText (Login.this, "Code has been Sent,please Check", Toast.LENGTH_SHORT).show ();

                sendOTP.setVisibility (View.INVISIBLE);
                mobile.setVisibility (View.INVISIBLE);
                format.setVisibility (View.INVISIBLE);

                Pass.setVisibility (View.VISIBLE);
                verify.setVisibility (View.VISIBLE);
            }
        };



    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingbar.dismiss ();
                            Toast.makeText (Login.this, "Congratulation , You are successfully Logged In", Toast.LENGTH_SHORT).show ();
                            sendUserToHomePage();
                        }
                        else
                        {

                            String message = task.getException ().toString ();
                            Toast.makeText (Login.this, "Error" + message, Toast.LENGTH_SHORT).show ();

                        }

                    }
                });
    }
    private void sendUserToHomePage() {
        Intent intent = new Intent (Login.this,profileActivity.class);
        startActivity (intent);
        finish();
    }
}
