package com.example.tabslayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.firebase.ui.database.FirebaseRecyclerOptions;

public class History extends AppCompatActivity {
    RecyclerView rvHistory;
    ExpenseAdaptor myAdaptor;
    DatabaseReference expenseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        rvHistory = findViewById(R.id.rvHistory);

        expenseRef = FirebaseDatabase.getInstance().getReference().child("Expense");

        initRecyclerView();
    }

    private void initRecyclerView() {
        // Set up FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Expense> options =
                new FirebaseRecyclerOptions.Builder<Expense>()
                        .setQuery(expenseRef, Expense.class)
                        .build();

        myAdaptor = new ExpenseAdaptor(options, this);
        rvHistory.setAdapter(myAdaptor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myAdaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdaptor.stopListening();
    }
}