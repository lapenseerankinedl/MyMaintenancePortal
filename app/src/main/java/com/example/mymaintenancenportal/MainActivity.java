package com.example.mymaintenancenportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Security;

public class MainActivity extends AppCompatActivity {
    EditText userName, password;
    Button btnSignIn, btnRegister;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnRegister = (Button) findViewById(R.id.btnReg);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(userName.getText().toString(), password.getText().toString());
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void signIn(final String id, final String p)
    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()) {
                    if (!id.isEmpty()) {
                        User user = dataSnapshot.child(id).getValue(User.class);
                        if (user.getPassword().equals(p)){
                            finish();
                            Toast.makeText(MainActivity.this,"Login Success", Toast.LENGTH_LONG).show();
                            Intent intphto = new Intent(MainActivity.this, Home.class);
                            intphto.putExtra("username", id);
                            intphto.putExtra("landlordEmail", user.getLandlordEmail());
                            startActivity(intphto);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Password Incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "User is not registered", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "User is not registered", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error with logging in user", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void registerUser()
    {
        finish();
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

}
