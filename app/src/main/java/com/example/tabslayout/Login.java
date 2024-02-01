package com.example.tabslayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    TextInputEditText etEmail,etPassword;
    TextView tvForgot,tvNewAccountRegister;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        tvNewAccountRegister=findViewById(R.id.tvNewAccountRegister);
        tvNewAccountRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,Register.class);
                startActivity(i);
                finish();
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this, ChangePasswordActivity.class);
                startActivity(i);
                finish();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etEmail.getText().toString().trim();
                String password=etPassword.getText().toString();

                if (email.isEmpty()||password.isEmpty()){
                    Toast.makeText(Login.this, "Please Fill requirements", Toast.LENGTH_SHORT).show();
                }
                else{
                    String ReplaceEmail = email.replace(".", ",");
                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(ReplaceEmail)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String storedPassword = snapshot.child("Password").getValue(String.class);
                                        if (password.equals(storedPassword)) {
                                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(Login.this, MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "Email not registered", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Login.this, "Data error", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            }



        });

    }
    private void init(){
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        tvForgot=findViewById(R.id.tvForgot);
        btnLogin=findViewById(R.id.btnLogin);
    }
}