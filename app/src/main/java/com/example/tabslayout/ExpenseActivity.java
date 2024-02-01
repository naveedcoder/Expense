package com.example.tabslayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ExpenseActivity extends AppCompatActivity {

    private RecyclerView rvExpense;
    private ExpenseAdaptor expenseAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        rvExpense = findViewById(R.id.rvExpense);

        String selectedCategory = getIntent().getStringExtra("selectedCategory");

        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference().child("Expense");
        Query query = expenseRef.orderByChild("Category").equalTo(selectedCategory);

        FirebaseRecyclerOptions<Expense> options =
                new FirebaseRecyclerOptions.Builder<Expense>()
                        .setQuery(query, Expense.class)
                        .build();

        expenseAdaptor = new ExpenseAdaptor(options,this);
        rvExpense.setAdapter(expenseAdaptor);
    }
    @Override
    public void onBackPressed() {
        // Check if there are fragments in the back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Pop the fragment from the back stack
            getSupportFragmentManager().popBackStack();
        } else {
            // If no fragments in the back stack, go back to CategoryFrag
            super.onBackPressed();
        }
    }
 
//    @Override
//    public void onBackPressed() {
//
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//
//            getSupportFragmentManager().popBackStack();
//        } else {
//
//            super.onBackPressed();
//        }
//    }



    @Override
    protected void onStart() {
        super.onStart();
        expenseAdaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        expenseAdaptor.stopListening();
    }
}
