package com.example.tabslayout;

import static android.app.ProgressDialog.show;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


public class HomeFrag extends Fragment {
    TextView tvIncomePrice,tvExpensePrice;
    FloatingActionButton BtnAddExpense;
    RecyclerView rvExpense;
   ExpenseAdaptor myAdaptor;
    public HomeFrag() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvIncomePrice = view.findViewById(R.id.tvIncomePrice);
        tvExpensePrice = view.findViewById(R.id.tvExpensePrice);
        rvExpense=view.findViewById(R.id.rvExpense);
        BtnAddExpense = view.findViewById(R.id.BtnAddExpense);
        init();
        DatabaseReference userIncomeRef = FirebaseDatabase.getInstance().getReference().child("UserIncome");
        userIncomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String income = snapshot.child("Income").getValue(String.class);
                    tvIncomePrice.setText(income);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference().child("Expense");
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              double  totalExpense = 0;
                for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);
                    if (expense != null) {
                        totalExpense += Double.parseDouble(expense.getPrice());
                    }
                }
                tvExpensePrice.setText(String.valueOf(totalExpense));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        BtnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(requireContext()).inflate(R.layout.alert_add_expense, null);
                TextView timeStamp = v.findViewById(R.id.timeStamp);
                TextInputEditText etTitle = v.findViewById(R.id.etTitle);
                TextInputEditText etDescription = v.findViewById(R.id.etDescription);
                TextInputEditText etCategory= v.findViewById(R.id.etCategory);
                TextInputEditText etPrice = v.findViewById(R.id.etPrice);

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss");
                Date date = new Date();
                timeStamp.setText(formatter.format(date));

                AlertDialog.Builder addExpenseDialog = new AlertDialog.Builder(requireContext())
                        .setTitle("Add New Expense")
                        .setView(v).setPositiveButton("save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String Title=etTitle.getText().toString().trim();
                                String Description=etDescription.getText().toString().trim();
                                String Price=etPrice.getText().toString().trim();
                                String Category=etCategory.getText().toString().trim();

                                HashMap<String,Object> data=new HashMap<>();
                                data.put("Title",Title);
                                data.put("Description",Description);
                                data.put("Price",Price);
                                data.put("Category",Category);
                                data.put("timeStamp",timeStamp.getText().toString());
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Expense")
                                        .push()
                                        .setValue(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Expense Added Successfully", Toast.LENGTH_SHORT).show();
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

                addExpenseDialog.create();
                addExpenseDialog.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    void init(){
        Query query = FirebaseDatabase.getInstance().getReference().child("Expense");

        FirebaseRecyclerOptions<Expense> options =
                new FirebaseRecyclerOptions.Builder<Expense>()
                        .setQuery(query, Expense.class)
                        .build();
        myAdaptor = new ExpenseAdaptor(options,getContext());
        rvExpense.setAdapter(myAdaptor);

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