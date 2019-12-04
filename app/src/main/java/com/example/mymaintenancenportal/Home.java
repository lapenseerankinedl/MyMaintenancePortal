package com.example.mymaintenancenportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    Button maintenanceRequest, viewRequests, viewHistory, logOutButton;
    TextView userView;
    String userName;
    String landlordEmail;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        Intent intentExtras = getIntent();

        userName = getIntent().getStringExtra("username");
        landlordEmail = getIntent().getStringExtra("landlordEmail");

        userView = (TextView) findViewById(R.id.userNameWritten);
        maintenanceRequest = (Button) findViewById(R.id.btn_make_request);
        viewRequests = (Button) findViewById(R.id.btn_view_request);
        viewHistory = (Button) findViewById(R.id.btn_view_history);
        logOutButton = (Button) findViewById(R.id.btn_log_out);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String landlord = dataSnapshot.child(userName).child("Landlord email").getValue(String.class);
                if (landlord.equals("None"))
                {
                    maintenanceRequest.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        displayUsername(userName, userView);

        maintenanceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMaintenanceRequest(userName);
            }
        });

        viewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openViewRequests(userName);
            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openViewHistory(userName);
            }
        });


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

    }

    public void displayUsername(String name, TextView user)
    {
        user.setText(name);
    }

    public void openMaintenanceRequest(String id)
    {
        finish();
        Intent intent = new Intent(Home.this, MaintenanceRequest.class);
        intent.putExtra("username", id);
        startActivity(intent);
    }

    public void openViewRequests(String id)
    {
        finish();
        Intent intent = new Intent(Home.this, ViewRequests.class);
        intent.putExtra("username", id);
        startActivity(intent);
    }

    public void openViewHistory(String id)
    {
        finish();
        Intent intent = new Intent(Home.this, ViewHistory.class);
        intent.putExtra("username", id);
        startActivity(intent);
    }


    public void logOut()
    {
        finish();
        Intent intent = new Intent(Home.this, MainActivity.class);
        startActivity(intent);
    }


}
