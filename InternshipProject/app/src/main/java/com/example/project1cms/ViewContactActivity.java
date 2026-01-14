package com.example.project1cms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewContactActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Contact> contactList;
    ContactAdapter adapter;
    DatabaseReference databaseContacts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_view_contact);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();
        adapter = new ContactAdapter(this, contactList);
        recyclerView.setAdapter(adapter);

        databaseContacts = FirebaseDatabase.getInstance().getReference("contacts");

        databaseContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                contactList.clear();
                for (DataSnapshot contactSnap : snapshot.getChildren()) {
                    Contact contact = contactSnap.getValue(Contact.class);
                    if (contact != null) {
                        contactList.add(contact);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ViewContactActivity.this,
                        "Failed: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
