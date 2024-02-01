package com.example.tabslayout;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {

    TextInputEditText etOldEmail, etNewPassword;
    Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etOldEmail = findViewById(R.id.etOldEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldEmail = etOldEmail.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString();

                if (oldEmail.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Please fill all requirements", Toast.LENGTH_SHORT).show();
                } else {
                    String replaceOldEmail = oldEmail.replace(".", ",");
                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(replaceOldEmail)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Update the password in the database
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child(replaceOldEmail)
                                                .child("Password").setValue(newPassword)
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ChangePasswordActivity.this, "Password changed successfully Login again", Toast.LENGTH_SHORT).show();
                                                        Intent i=new Intent(ChangePasswordActivity.this, Login.class);
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this, "Old Email not registered", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(ChangePasswordActivity.this, "Data error", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}