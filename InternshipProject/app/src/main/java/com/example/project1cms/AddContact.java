package com.example.project1cms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContact extends AppCompatActivity {

    EditText etName, etMobile, etEmail;
    Button btnAdd;

    DatabaseReference databaseContacts;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_add_contact);

        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        btnAdd = findViewById(R.id.btnAdd);

        databaseContacts = FirebaseDatabase.getInstance().getReference("contacts");

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (name.isEmpty() || mobile.isEmpty() || email.isEmpty()) {
                Toast.makeText(AddContact.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use email as key
            Contact contact = new Contact(name, mobile, email);

            databaseContacts.child(email.replace(".", "_")).setValue(contact)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddContact.this, "Contact added!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddContact.this, ViewContactActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddContact.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}