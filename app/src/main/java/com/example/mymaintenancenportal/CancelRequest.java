package com.example.mymaintenancenportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CancelRequest extends AppCompatActivity {

    Button cancel, submit;
    EditText reason;
    String userName;
    String requestID;
    String editOption;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_request);

        Intent intentExtras = getIntent();
        Bundle bundle = intentExtras.getExtras();
        userName = getIntent().getStringExtra("username");
        requestID = getIntent().getStringExtra("id");

        databaseReference = FirebaseDatabase.getInstance().getReference();


        cancel = (Button) findViewById(R.id.btnCancelCancellation);
        submit = (Button) findViewById(R.id.btnSubmitCancel);
        reason = (EditText) findViewById(R.id.cancelReasonText);

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
        final String r = reason.getText().toString().trim();
        if (TextUtils.isEmpty(r)){
            Toast.makeText(this, "Please put a reason for cancelling request", Toast.LENGTH_LONG).show();
        }
        else
        {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    databaseReference.child("Requests").child(id).child("status").setValue("Request has been cancelled");
                    databaseReference.child("Requests").child(id).child("cancel reason").setValue(r);
                    Toast.makeText(CancelRequest.this, "Request has been cancelled", Toast.LENGTH_LONG).show();
                    goToRequests(userName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(CancelRequest.this, "Status error", Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    public void goToRequests(String id)
    {
        finish();
        Intent intent = new Intent(CancelRequest.this, ViewRequests.class);
        intent.putExtra("username", userName);
        startActivity(intent);
    }


}
