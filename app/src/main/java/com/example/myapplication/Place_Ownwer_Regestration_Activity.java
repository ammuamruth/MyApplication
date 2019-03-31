package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Place_Ownwer_Regestration_Activity extends AppCompatActivity {
    TextView login;
    Button btn;
    EditText email;

    String[] country = { "Select Number of slots", "1", "2", "3", "4"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place__ownwer__regestration_);

        login = (TextView)findViewById(R.id.login);
        btn = (Button)findViewById(R.id.Register);
        email = (EditText)findViewById(R.id.email);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(Place_Ownwer_Regestration_Activity.this,Place_Owners_Login.class);
                startActivity(i);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validemail();
                Intent i = new Intent(Place_Ownwer_Regestration_Activity.this,Place_Owners_Profile.class);
                startActivity(i);
                finish();
            }
        });



    }



    public  void validemail(){

        String mail = email.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

// onClick of button perform this simplest code.
        if (mail.matches(emailPattern))
        {
            Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
        }


    }
}
