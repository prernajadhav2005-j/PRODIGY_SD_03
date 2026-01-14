package com.example.project1cms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    Context context;
    ArrayList<Contact> contactList;
    DatabaseReference databaseContacts;

    public ContactAdapter(Context context, ArrayList<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        databaseContacts = FirebaseDatabase.getInstance().getReference("contacts");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Contact contact = contactList.get(position);

        holder.txtName.setText(contact.getName());
        holder.txtMobile.setText("Mobile: " + contact.getMobile());
        holder.txtEmail.setText("Email: " + contact.getEmail());

        // ✅ EDIT BUTTON CLICK
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditContactActivity.class);
            intent.putExtra("name", contact.getName());
            intent.putExtra("mobile", contact.getMobile());
            intent.putExtra("email", contact.getEmail());
            context.startActivity(intent);
        });

        // ✅ DELETE BUTTON CLICK
        //holder.btnDelete.setOnClickListener(v -> showDeleteDialog(contact));
        holder.btnDelete.setOnClickListener(v -> {
            Intent intent = new Intent(context, DeleteContactActivity.class);
            intent.putExtra("name", contact.getName());
            intent.putExtra("mobile", contact.getMobile());
            intent.putExtra("email", contact.getEmail());
            context.startActivity(intent);
        });
    }

    private void showDeleteDialog(Contact contact) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton("Yes", (dialog, which) -> deleteContact(contact))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteContact(Contact contact) {

        databaseContacts.addListenerForSingleValueEvent(
                new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {

                        for (com.google.firebase.database.DataSnapshot snap : snapshot.getChildren()) {
                            Contact c = snap.getValue(Contact.class);

                            if (c != null &&
                                    c.getName().equals(contact.getName()) &&
                                    c.getMobile().equals(contact.getMobile())) {

                                snap.getRef().removeValue();
                                Toast.makeText(context,
                                        "Contact Deleted",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                        Toast.makeText(context,
                                "Delete Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtMobile, txtEmail;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
