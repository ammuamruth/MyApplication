package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Place_Owner_Dashboard extends AppCompatActivity {

    Button correctButton,wrongButton;
    AlertDialog.Builder builder;
    CardView card1;
    private String user;

    FirebaseAuth mAuth;
    DatabaseReference rootRef,demoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place__owner__dashboard);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getUid();

        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child(user).child(user);

        correctButton = (Button)findViewById(R.id.correct);
        wrongButton = (Button)findViewById(R.id.wrong);
        builder = new AlertDialog.Builder (this);
        card1 = (CardView)findViewById(R.id.cardView);

        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(Place_Owner_Dashboard.this, "Cancelled", Toast.LENGTH_SHORT).show();

                builder.setMessage("Confirm Booking ?");


                builder.setPositiveButton("Yes", null);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(Place_Owner_Dashboard.this, "Booking Confirmed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent (Place_Owner_Dashboard.this,Place_owner_Payment_Section.class);
                        startActivity (intent);
                    }
                });


                builder.setNegativeButton("Cancel", null);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Toast.makeText(Place_Owner_Dashboard.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent (Book.this,Pay_Full.class);
                        startActivity (intent);*/
                    }
                });



                AlertDialog alert = builder.create();
                alert.setTitle("Are you sure");
                alert.show();

            }

        });


        wrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                builder.setMessage("Wrong Vehicle");


                builder.setPositiveButton("Yes", null);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        card1.setVisibility(View.INVISIBLE);
                        Toast.makeText(Place_Owner_Dashboard.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent (Place_Owner_Dashboard.this,Place_Owner_Regestration_Activity.class);
                        startActivity (intent);*/
                    }
                });


                builder.setNegativeButton("Cancel", null);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(Place_Owner_Dashboard.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent (Book.this,Pay_Full.class);
                        startActivity (intent);*/
                    }
                });



                AlertDialog alert = builder.create();
                alert.setTitle("Are you sure");
                alert.show();


            }

        });
    }
}
