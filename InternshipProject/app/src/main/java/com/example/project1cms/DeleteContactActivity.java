package com.example.project1cms;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteContactActivity extends AppCompatActivity {

    Button btnConfirmDelete, btnCancel;
    DatabaseReference databaseContacts;

    String name, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contact);

        btnConfirmDelete = findViewById(R.id.btnConfirmDelete);
        btnCancel = findViewById(R.id.btnCancel);

        databaseContacts = FirebaseDatabase.getInstance().getReference("contacts");

        // Get data from intent
        name = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");

        btnConfirmDelete.setOnClickListener(v -> deleteContact());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void deleteContact() {

        databaseContacts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot contactSnap : snapshot.getChildren()) {

                    Contact contact = contactSnap.getValue(Contact.class);

                    if (contact != null &&
                            contact.getName().equals(name) &&
                            contact.getMobile().equals(mobile)) {

                        contactSnap.getRef().removeValue();

                        Toast.makeText(DeleteContactActivity.this,
                                "Contact Deleted Successfully",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DeleteContactActivity.this,
                        "Delete Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
