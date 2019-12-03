package com.example.mymaintenancenportal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewHistory extends AppCompatActivity{

    String userName;
    Button back;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Request> list;
    MyAdapterHistory adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        recyclerView = (RecyclerView) findViewById(R.id.viewRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intentExtras = getIntent();
        Bundle bundle = intentExtras.getExtras();

        userName = getIntent().getStringExtra("username");
        reference = FirebaseDatabase.getInstance().getReference();
        back = (Button) findViewById(R.id.btn_back_history);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Request>();
                String landlord;

                if(!dataSnapshot.child("Users").child(userName).child("Landlord email").getValue(String.class).equals("None"))
                {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.child("Requests").getChildren())
                    {
                        Request r = new Request();
                        String name = dataSnapshot1.child("User").getValue(String.class);
                        if (userName.equals(name))
                        {
                            String stat = dataSnapshot1.child("status").getValue(String.class);
                            if (stat.equals("Request has been cancelled")
                                    || stat.equals("Landlord has completed the request"))
                            {
                                String status = dataSnapshot1.child("status").getValue(String.class);
                                String urgency = dataSnapshot1.child("Urgency").getValue(String.class);
                                String image = dataSnapshot1.child("Image Path").getValue(String.class);
                                String des = dataSnapshot1.child("description").getValue(String.class);
                                String rea = dataSnapshot1.child("cancel reason").getValue(String.class);
                                String key = dataSnapshot1.getKey();
                                r.setTenantName(name);
                                r.setUrgency(urgency);
                                r.setImage(image);
                                r.setRequestText(des);
                                r.setStatus(stat);
                                r.setCancelReason(rea);
                                r.setRequestID(key);
                                list.add(r);
                            }
                        }

                    }
                    adapter = new MyAdapterHistory(ViewHistory.this, list, userName, false);
                    recyclerView.setAdapter(adapter);

                }
                else
                {
                    String email = dataSnapshot.child("Users").child(userName).child("email").getValue(String.class);
                    for(DataSnapshot dataSnapshot1: dataSnapshot.child("Requests").getChildren())
                    {
                        Request r = new Request();
                        String name = dataSnapshot1.child("User").getValue(String.class);
                        landlord = dataSnapshot1.child("Landlord Email").getValue(String.class);
                        if (email.equals(landlord))
                        {
                            String stat = dataSnapshot1.child("status").getValue(String.class);
                            if (stat.equals("Request has been cancelled")
                                    || stat.equals("Landlord has completed the request"))
                            {
                                String urgency = dataSnapshot1.child("Urgency").getValue(String.class);
                                String image = dataSnapshot1.child("Image Path").getValue(String.class);
                                String key = dataSnapshot1.getKey();
                                String des = dataSnapshot1.child("description").getValue(String.class);
                                String rea = dataSnapshot1.child("cancel reason").getValue(String.class);
                                r.setTenantName(name);
                                r.setUrgency(urgency);
                                r.setImage(image);
                                r.setRequestText(des);
                                r.setStatus(stat);
                                r.setCancelReason(rea);
                                r.setRequestID(key);
                                list.add(r);
                            }

                        }
                    }
                    adapter = new MyAdapterHistory(ViewHistory.this, list, userName, true);
                    recyclerView.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewHistory.this, "Something is wrong", Toast.LENGTH_LONG).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome(userName);
            }
        });

    }

    public void goHome(String id)
    {
        finish();
        Intent intent = new Intent(ViewHistory.this, Home.class);
        intent.putExtra("username", id);
        startActivity(intent);
    }

}
