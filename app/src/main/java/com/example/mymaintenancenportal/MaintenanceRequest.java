package com.example.mymaintenancenportal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class MaintenanceRequest extends AppCompatActivity {
    EditText desc, username;
    RadioGroup options;
    ImageView image;
    Button submitRequest, cancelRequest;
    String urgencyOption;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;
    private int Gallary_intent = 2;
    Uri uriProfileImage;
    StorageReference imagePath;
    String maintenanceRequestImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_request);
        desc = (EditText) findViewById(R.id.description_text);
        username = (EditText) findViewById(R.id.userName_text);
        image = (ImageView) findViewById(R.id.imageView);
        cancelRequest = (Button) (Button) findViewById(R.id.btnCancelRequest);
        submitRequest = (Button) findViewById(R.id.btnSubmitRequest);
        databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });
        submitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMaintenanceRequest();
            }
        });
    }

    public void submitMaintenanceRequest()
    {
        String description = desc.getText().toString().trim();
        String urgency = urgencyOption;

        if (TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_LONG).show();
        }
        else if (urgency == null)
        {
            Toast.makeText(this, "Please choose an urgency option", Toast.LENGTH_LONG).show();
        }
        else
        {
            String id = databaseReference.push().getKey();
            Request request = new Request(id, description, "", urgency);
            databaseReference.child(id).child("id").setValue(id.toString());
            databaseReference.child(id).child("User").setValue("");
            databaseReference.child(id).child("description").setValue(description.toString());
            databaseReference.child(id).child("Urgency").setValue(urgency.toString());
            databaseReference.child(id).child("Landlord Email").setValue("");
            databaseReference.child(id).child("Image Path").setValue(imagePath.toString());
            Toast.makeText(this, "Request has been created", Toast.LENGTH_LONG).show();
            clearEntries();
            goToHome();
        }
    }

    public void goToHome()
    {
        Intent intent = new Intent(MaintenanceRequest.this, Home.class);
        startActivity(intent);
    }

    public void clearEntries()
    {
        desc.getText().clear();
    }

    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.lowUrgency:
                if (checked)
                    urgencyOption = "Low";
                break;
            case R.id.mediumUrgency:
                if (checked)
                    urgencyOption = "Medium";
                break;
            case R.id.highUrgency:
                if (checked)
                    urgencyOption = "High";
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallary_intent && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            uriProfileImage = data.getData();
            image.setImageURI(uriProfileImage);
            imagePath = FirebaseStorage.getInstance().getReference().child("Maintenance Image").child(uriProfileImage.getLastPathSegment());
            imagePath.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MaintenanceRequest.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MaintenanceRequest.this, "Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void btnImage(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Gallary_intent);
    }
}
