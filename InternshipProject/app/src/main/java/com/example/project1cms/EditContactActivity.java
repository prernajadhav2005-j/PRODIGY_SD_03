package com.example.project1cms;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditContactActivity extends AppCompatActivity {

    EditText edtName, edtMobile, edtEmail;
    Button btnUpdate;

    DatabaseReference databaseContacts;

    String oldName, oldMobile, oldEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        edtName = findViewById(R.id.edtName);
        edtMobile = findViewById(R.id.edtMobile);
        edtEmail = findViewById(R.id.edtEmail);
        btnUpdate = findViewById(R.id.btnUpdate);

        databaseContacts = FirebaseDatabase.getInstance().getReference("contacts");

        // Get data from intent
        oldName = getIntent().getStringExtra("name");
        oldMobile = getIntent().getStringExtra("mobile");
        oldEmail = getIntent().getStringExtra("email");

        // Load existing data
        edtName.setText(oldName);
        edtMobile.setText(oldMobile);
        edtEmail.setText(oldEmail);

        btnUpdate.setOnClickListener(v -> updateContact());
    }

    private void updateContact() {

        String newName = edtName.getText().toString().trim();
        String newMobile = edtMobile.getText().toString().trim();
        String newEmail = edtEmail.getText().toString().trim();

        if (newName.isEmpty() || newMobile.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Find correct contact in Firebase and update
        databaseContacts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot contactSnap : snapshot.getChildren()) {

                    Contact contact = contactSnap.getValue(Contact.class);

                    if (contact != null &&
                            contact.getName().equals(oldName) &&
                            contact.getMobile().equals(oldMobile)) {

                        // Update values
                        contactSnap.getRef().child("name").setValue(newName);
                        contactSnap.getRef().child("mobile").setValue(newMobile);
                        contactSnap.getRef().child("email").setValue(newEmail);

                        Toast.makeText(EditContactActivity.this,
                                "Contact Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EditContactActivity.this,
                        "Update Failed: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
