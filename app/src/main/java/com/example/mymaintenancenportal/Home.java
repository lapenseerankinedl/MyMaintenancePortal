package com.example.mymaintenancenportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    Button maintenanceRequest;
    TextView userView;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Intent intentExtras = getIntent();
        Bundle bundle = intentExtras.getExtras();

        userName = getIntent().getStringExtra("username");
        userView = (TextView) findViewById(R.id.userNameWritten);
        displayUsername(userName, userView);


        maintenanceRequest = (Button) findViewById(R.id.btn_make_request);
        maintenanceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMaintenanceRequest();
            }
        });
    }

    public void displayUsername(String name, TextView user)
    {
        user.setText(name);
    }

    public void openMaintenanceRequest()
    {
        Intent intent = new Intent(Home.this, MaintenanceRequest.class);
        startActivity(intent);
    }
}
