package com.example.project1cms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class contact_management extends AppCompatActivity {

    Button btnAdd, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_management);

        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);


        btnAdd.setOnClickListener(v -> {
            Toast.makeText(this, "Add Contact Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(contact_management.this, AddContact.class));
        });
        btnView.setOnClickListener(v -> {
            Toast.makeText(this, "View Contact List Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(contact_management.this, ViewContactActivity.class));
        });
    }
}