package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Launcher_Activity extends AppCompatActivity {

    Button customer,place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_);

        customer = (Button)findViewById(R.id.user);
        place = (Button)findViewById(R.id.placeOwner);


        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Launcher_Activity.this,Login.class);
                startActivity(i);
            }
        });

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Launcher_Activity.this,Place_Owners_Login.class);
                startActivity(i);
                //Toast.makeText(Launcher_Activity.this, "Im a Place Owner", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
