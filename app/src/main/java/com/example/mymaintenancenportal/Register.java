package com.example.mymaintenancenportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    EditText userText, nameText, userEmail, userPassword, confirmPass, landlordEmail;
    CheckBox checked_yes;
    boolean is_checked;
    Button btnSave, btnCancel;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth = FirebaseAuth.getInstance();
        userText = (EditText) findViewById(R.id.uniqueName);
        nameText = (EditText) findViewById(R.id.name);
        userEmail = (EditText) findViewById(R.id.regEmail);
        userPassword = (EditText) findViewById(R.id.regPassword);
        confirmPass = (EditText) findViewById(R.id.confirmPassword);
        checked_yes = (CheckBox) findViewById(R.id.check_yes);
        boolean checked = false;
        landlordEmail = (EditText) findViewById(R.id.landlord_email);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArrayList();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    private void addArrayList()
    {
        final String unique = userText.getText().toString().trim();
        final String name = nameText.getText().toString().trim();
        final String email = userEmail.getText().toString().trim();
        final String password = userPassword.getText().toString().trim();
        String confirmPassword = confirmPass.getText().toString().trim();
        final boolean checked = is_checked;
        final String emailLandlord = landlordEmail.getText().toString().trim();

        if(TextUtils.isEmpty(unique))
        {
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
        }
        else if(!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Please make sure to enter the same password", Toast.LENGTH_LONG).show();
        }
        else if (password.length() < 8)
        {
            Toast.makeText(this, "Please make the password 8 characters or longer", Toast.LENGTH_LONG).show();
        }
        else if(checked && TextUtils.isEmpty(emailLandlord))
        {
            Toast.makeText(this, "Please enter your landlord's email", Toast.LENGTH_LONG).show();

        }
        else
        {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child(unique).exists())
                    {
                        String id = databaseReference.push().getKey();
                        User user = new User(id, unique, name, email, password);
                        databaseReference.child(unique).child("id").setValue(id.toString());
                        databaseReference.child(unique).child("username").setValue(unique.toString());
                        databaseReference.child(unique).child("name").setValue(name.toString());
                        databaseReference.child(unique).child("email").setValue(email.toString());
                        databaseReference.child(unique).child("password").setValue(password.toString());
                        if (checked)
                        {
                            databaseReference.child(unique).child("Landlord email").setValue(emailLandlord.toString());
                            user.setLandlordEmail(emailLandlord);
                        }
                        else
                        {
                            databaseReference.child(unique).child("Landlord email").setValue("None".toString());
                            user.setLandlordEmail("None");
                        }

                        Toast.makeText(Register.this, "User is added", Toast.LENGTH_LONG).show();
                        openHome(unique, emailLandlord);
                    }
                    else
                    {
                        Toast.makeText(Register.this, "This username is taken, please choose another one", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Register.this, "Error with registering user", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void openHome(String username, String landlordEmail)
    {
        finish();
        Intent intent = new Intent(Register.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("username", username);
        intent.putExtra("landlordEmail", landlordEmail);
        startActivity(intent);
    }

    public void goBack()
    {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view)
    {
        is_checked = ((CheckBox) view).isChecked();
        if (is_checked)
        {
            landlordEmail.setVisibility(View.VISIBLE);
        }
        else
        {
            landlordEmail.setVisibility(View.GONE);
        }
    }
}
