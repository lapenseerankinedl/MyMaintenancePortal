package com.example.mymaintenancenportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditRequest extends AppCompatActivity {

    RadioGroup options;
    Button cancel, submit;
    String userName;
    String requestID;
    String editOption;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);

        Intent intentExtras = getIntent();
        Bundle bundle = intentExtras.getExtras();
        userName = getIntent().getStringExtra("username");
        requestID = getIntent().getStringExtra("id");

        databaseReference = FirebaseDatabase.getInstance().getReference();


        cancel = (Button) findViewById(R.id.btnCancelEdit);
        submit = (Button) findViewById(R.id.btnSubmitEdit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRequest(requestID);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRequests(userName);
            }
        });


    }

    public void updateRequest(final String id)
    {
        final String option = editOption;
        if (option == null)
        {
            Toast.makeText(this, "Please choose a new value for status", Toast.LENGTH_LONG).show();
        }
        else {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    databaseReference.child("Requests").child(id).child("status").setValue(option);
                    Toast.makeText(EditRequest.this, "Status has been updated", Toast.LENGTH_LONG).show();
                    goToRequests(userName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EditRequest.this, "Status error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void goToRequests(String id)
    {
        finish();
        Intent intent = new Intent(EditRequest.this, ViewRequests.class);
        intent.putExtra("username", userName);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.seenRequest:
                if (checked)
                    editOption = "Landlord has seen the Request";
                break;
            case R.id.preparingRequest:
                if (checked)
                    editOption = "Landlord is preparing to complete the request";
                break;
            case R.id.completedRequest:
                if (checked)
                    editOption = "Landlord has completed the request";
                break;
        }
    }


}
