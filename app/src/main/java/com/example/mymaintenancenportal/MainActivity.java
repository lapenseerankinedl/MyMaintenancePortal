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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
        /*
        String email = userName.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
        }
        else
        {

            mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        finish();
                        Toast.makeText(MainActivity.this,"Login Success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Email or Password is not correct", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        //*/

        ///*
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()) {
                    if (!id.isEmpty()) {
                        User user=dataSnapshot.child(id).getValue(User.class);
                        if (user.getPassword().equals(p)){
                            username = id;
                            Toast.makeText(MainActivity.this,"Login Success", Toast.LENGTH_LONG).show();
                            Intent intphto =new Intent(getApplicationContext(), Home.class);
                            intphto.putExtra("username", username);
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

            }
        });

         //*/

    }


    public void registerUser()
    {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

}
