package com.example.tabslayout;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CategoryFrag extends Fragment {

    TextView tvPrice;
    ImageView ivDots;
    Button btnAddIncome;
    RecyclerView rvCategory;
    CategoryAdaptor myAdaptor;
    FloatingActionButton BtnAddCategory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPrice = view.findViewById(R.id.tvPrice);
        btnAddIncome = view.findViewById(R.id.btnAddIncome);
        ivDots = view.findViewById(R.id.ivDots);
        BtnAddCategory = view.findViewById(R.id.BtnAddCategory);
        rvCategory=view.findViewById(R.id.rvCategory);
        init();
        DatabaseReference userIncomeRef = FirebaseDatabase.getInstance().getReference().child("UserIncome");
        userIncomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String income = snapshot.child("Income").getValue(String.class);
                    tvPrice.setText(income);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View incomeView = LayoutInflater.from(getContext()).inflate(R.layout.alert_income, null);
                TextInputEditText etIncome = incomeView.findViewById(R.id.etIncome);

                AlertDialog.Builder addIncomeDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Add Monthly Income")
                        .setView(incomeView)
                        .setPositiveButton("save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String income = etIncome.getText().toString().trim();
                                userIncomeRef.child("Income").setValue(income);
                                Toast.makeText(getContext(), "Income added", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                addIncomeDialog.create().show();
            }
        });


        BtnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(requireContext()).inflate(R.layout.alert_category_design, null);
                TextInputEditText etNewCategory = v.findViewById(R.id.etNewCategory);


                AlertDialog.Builder addCategoryDialog = new AlertDialog.Builder(requireContext())
                        .setTitle("Add New Category")
                        .setView(v).setPositiveButton("save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String Category=etNewCategory.getText().toString().trim();



                                HashMap<String,Object> data=new HashMap<>();
                                data.put("Category",Category);


                                FirebaseDatabase.getInstance().getReference()
                                        .child("Category")
                                        .push()
                                        .setValue(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "CategoryAdded Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                addCategoryDialog.create();
                addCategoryDialog.show();
            }
        });




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }


    public void init(){
        Query query = FirebaseDatabase.getInstance().getReference().child("Category");

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, Category.class)
                        .build();
        myAdaptor = new CategoryAdaptor(options,getContext());
        rvCategory.setAdapter(myAdaptor);

    }

    @Override
    public void onStart() {
        super.onStart();
        myAdaptor.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myAdaptor.stopListening();
    }
}