package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Place_Owners_Profile extends AppCompatActivity implements LocationListener {

    private static final int GALLERY_PICK = 1;
    private FirebaseAuth mAuth;
    private DatabaseReference myref;
    private StorageReference PostImagesRef;
    private Uri ImageUri;

    Button b1, Update;
    private static final int gallery = 1;
    ImageButton profileImage;
    private String user;
    private EditText uNmae, E_mail, add;
    private TextView number;
    private ProgressDialog loadingBar;
    private String saveCurrentDate, saveCurrentTime, postRandomName;

    private String address;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat, city, you_are_here;
    String lat;
    String provider;
    protected double latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    private Geocoder geocoder;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place__owners__profile);

        fetchLocationData();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getUid();
        myref = FirebaseDatabase.getInstance().getReference("Place_Owners").child(user);
        PostImagesRef = FirebaseStorage.getInstance().getReference().child("Place_Owner_Images");

        Update = (Button) findViewById(R.id.Update_Profile);
        add = (EditText) findViewById(R.id.address);
        profileImage = (ImageButton) findViewById(R.id.agentImage);
        uNmae = (EditText) findViewById(R.id.name);
        E_mail = (EditText) findViewById(R.id.email);
        loadingBar = new ProgressDialog(this);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = uNmae.getText().toString().trim();
                String mail = E_mail.getText().toString().trim();
                String KYC = add.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(Place_Owners_Profile.this, "Plaese fill all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(Place_Owners_Profile.this, "Please Fill all the Details", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(KYC)) {
                    Toast.makeText(Place_Owners_Profile.this, "Please Fill all the details", Toast.LENGTH_SHORT).show();
                    return;
                }

                placeholder_data usersprofile = new placeholder_data(userName, mail, KYC);
                FirebaseUser user = mAuth.getCurrentUser();
                myref.child(user.getUid()).setValue(usersprofile);

                Intent i = new Intent(Place_Owners_Profile.this, Main3Activity.class);
                startActivity(i);
                finish();
                Toast.makeText(Place_Owners_Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }


        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //gallery opening
                Intent Galleryintent = new Intent();
                Galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                Galleryintent.setType("image/*");
                startActivityForResult(Galleryintent, gallery);
            }
        });

        //fetch image from database
        myref.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String image = dataSnapshot.getValue().toString();
                    Glide.with(getApplicationContext())
                            .load(image)
                            .into(profileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //cropping image
        if (requestCode == gallery && resultCode == RESULT_OK && data != null) {
            loadingBar.setTitle("Upload");
            loadingBar.setMessage("Please Wait while image to upload...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Uri ImageUri = data.getData();


            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();


                final StorageReference filePath = PostImagesRef.child(user + ".jpg");

                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String downloadUrl = uri.toString();
                                myref.child("image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Place_Owners_Profile.this, "Image Uploaded successfully", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                } else {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(Place_Owners_Profile.this, "Error Occured" + message, Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }


                                            }
                                        });
                            }
                        });
                    }
                });
            }
        }
        LocationManager locationManager = (LocationManager) getSystemService (LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, 30000, 0, this);




        Criteria criteria = new Criteria ();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location == null) {
            Toast.makeText(getApplicationContext(), "GPS signal not found",
                    3000).show();
        }
        if (location != null) {
            Log.e("locatin", "location--" + location);

            Log.e("latitude at beginning",
                    "@@@@@@@@@@@@@@@" + location.getLatitude());
            onLocationChanged(location);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        List<Address> addresses;

        geocoder = new Geocoder(this, Locale.getDefault());

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.e("latitude", "latitude--" + latitude);

        try {
            Log.e("latitude", "inside latitude--" + latitude);
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                //String state = addresses.get(0).getAdminArea();
                //String country = addresses.get(0).getCountryName();
                //String postalCode = addresses.get(0).getPostalCode();
                //String knownName = addresses.get(0).getFeatureName();

                add.setText(address + " " + city + " " );


            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude", "status");

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude", "enable");


    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude", "disable");
    }


    public void fetchLocationData() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!statusOfGPS) {
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return;
        }
        Toast.makeText(this, "Location is triggered from here acc", Toast.LENGTH_SHORT).show();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                Intent gpsOptionsIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(gpsOptionsIntent);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Toast.makeText(this, "Toast call is triggered from abc", Toast.LENGTH_SHORT).show();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }
}