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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MaintenanceRequest extends AppCompatActivity {
    EditText desc;
    RadioGroup options;
    ImageView image;
    Button submitRequest, cancelRequest;

    String urgencyOption;
    String userName;
    String landlord;
    DatabaseReference databaseReferenceRequest;
    DatabaseReference databaseReferenceUser;
    DatabaseReference databaseReference;
    private int Gallary_intent = 2;
    Uri uriProfileImage;
    StorageReference imagePath;
    String imagePathString;
    String maintenanceRequestImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_request);

        Intent intentExtras = getIntent();
        Bundle bundle = intentExtras.getExtras();

        userName = getIntent().getStringExtra("username");


        desc = (EditText) findViewById(R.id.description_text);
        image = (ImageView) findViewById(R.id.imageView);
        cancelRequest = (Button) findViewById(R.id.btnCancelRequest);
        submitRequest = (Button) findViewById(R.id.btnSubmitRequest);
        imagePathString = "";

        databaseReferenceRequest = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference();


        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome(userName);
            }
        });
        submitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMaintenanceRequest(userName);
            }
        });
    }

    public void submitMaintenanceRequest(final String userName)
    {
        final String description = desc.getText().toString().trim();
        final String urgency = urgencyOption;


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

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    landlord = dataSnapshot.child("Users").child(userName).child("Landlord email").getValue(String.class);
                    String id = databaseReference.push().getKey();
                    Request request = new Request(id.toString(), description.toString(), userName.toString(), urgency.toString(), landlord.toString(), imagePath.toString());
                    databaseReference.child("Requests").child(id).child("id").setValue(id.toString());
                    databaseReference.child("Requests").child(id).child("User").setValue(userName.toString());
                    databaseReference.child("Requests").child(id).child("description").setValue(description.toString());
                    databaseReference.child("Requests").child(id).child("Urgency").setValue(urgency.toString());
                    databaseReference.child("Requests").child(id).child("Landlord Email").setValue(landlord.toString());
                    databaseReference.child("Requests").child(id).child("Image Path").setValue(imagePath.toString());
                    databaseReference.child("Requests").child(id).child("status").setValue("Landlord has not viewed the request");
                    Toast.makeText(MaintenanceRequest.this, "Request has been created", Toast.LENGTH_LONG).show();
                    clearEntries();
                    goToHome(userName);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public void goToHome(String id)
    {
        finish();
        Intent intent = new Intent(MaintenanceRequest.this, Home.class);
        intent.putExtra("username", id);
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
            //imagePath = FirebaseStorage.getInstance().getReference().child("Maintenance Image").child(uriProfileImage.getLastPathSegment());
            imagePath = FirebaseStorage.getInstance().getReference().child("Maintenance Image").child(uriProfileImage.toString());
            submitRequest.setVisibility(View.GONE);
            imagePath.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MaintenanceRequest.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    submitRequest.setVisibility(View.VISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MaintenanceRequest.this, "Not Uploaded, please try again", Toast.LENGTH_SHORT).show();
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
