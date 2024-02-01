package com.example.tabslayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphFrag extends Fragment implements DateAdaptor.OnItemClickListener {

    private ImageView arrow;
    private RecyclerView recyclerViewDates;
    private DateAdaptor dateAdapter;

    private RecyclerView recyclerViewExpenses;
    private FirebaseRecyclerAdapter<Expense, ExpenseViewHolder> expenseAdapter;
    private LinearLayout expenseDetailsLayout;

    public GraphFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        arrow = view.findViewById(R.id.arrow);
        recyclerViewDates = view.findViewById(R.id.recyclerViewDates);
        recyclerViewExpenses = view.findViewById(R.id.recyclerViewExpenses);
        expenseDetailsLayout = view.findViewById(R.id.expenseDetailsLayout);

        setupRecyclerView();

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).switchToHomeFragment();
                }
            }
        });

        return view;
    }

    private void setupRecyclerView() {

        Calendar calendar = Calendar.getInstance();
        List<DateModel> dateList = new ArrayList<>();

        for (int i = 0; i < 365; i++) {
            DateModel dateModel = new DateModel();
            dateModel.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime()));
            dateModel.setTimestamp(System.currentTimeMillis());
            dateList.add(dateModel);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        dateAdapter = new DateAdaptor(dateList, this);
        recyclerViewDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewDates.setAdapter(dateAdapter);
        dateAdapter.setOnItemClickListener(this);

        Query expenseQuery = FirebaseDatabase.getInstance().getReference().child("Expense");
        FirebaseRecyclerOptions<Expense> expenseOptions = new FirebaseRecyclerOptions.Builder<Expense>()
                .setQuery(expenseQuery, Expense.class)
                .build();

        expenseAdapter = new FirebaseRecyclerAdapter<Expense, ExpenseViewHolder>(expenseOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Expense model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_expense_design, parent, false);
                return new ExpenseViewHolder(view);
            }
        };

        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewExpenses.setAdapter(expenseAdapter);
    }

    @Override
    public void onItemClick(View view, String selectedDate) {
        Log.d("GraphFrag", "onItemClick: Date - " + selectedDate);
        loadExpensesForDate(selectedDate);
        int position = dateAdapter.getSelectedPosition();
        dateAdapter.setSelectedPosition(position);
    }

    private void loadExpensesForDate(String selectedDate) {
        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference().child("Expense");
        String formattedSelectedDate = convertDateFormat(selectedDate);
        String startDate = formattedSelectedDate + " 00:00:00";
        String endDate = formattedSelectedDate + " 23:59:59";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            Date startDateObj = dateFormat.parse(startDate);
            Date endDateObj = dateFormat.parse(endDate);

            // Convert start and end dates to milliseconds
            long startTimeMillis = startDateObj.getTime();
            long endTimeMillis = endDateObj.getTime() + 999;

            Query query = expenseRef.orderByChild("timeStamp")
                    .startAt(startTimeMillis)
                    .endAt(endTimeMillis);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Expense> expenses = new ArrayList<>();
                    for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                        Expense expense = expenseSnapshot.getValue(Expense.class);
                        if (expense != null) {
                            expenses.add(expense);
                        }
                    }
                    Log.d("GraphFrag", "onDataChange: Expenses count - " + expenses.size());
                    displayExpenses(expenses, selectedDate);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    // Convert date format if needed
    private String convertDateFormat(String inputDate) {

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate;
        }
    }

    private void displayExpenses(List<Expense> expenses, String selectedDate) {

        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getTimeStamp().equals(selectedDate)) {
                filteredExpenses.add(expense);
            }
        }

        displayExpenseDetails(filteredExpenses);
        hidePastExpenses(expenses, selectedDate);
    }

    private void displayExpenseDetails(List<Expense> expenses) {
        expenseDetailsLayout.removeAllViews();

        for (Expense expense : expenses) {
            TextView textView = new TextView(getContext());
            textView.setText(String.format(Locale.getDefault(), "Title: %s\nDescription: %s\nPrice: %s\nCategory: %s",
                    expense.getTitle(), expense.getDescription(), expense.getPrice(), expense.getCategory()));
            expenseDetailsLayout.addView(textView);
        }

        expenseDetailsLayout.setVisibility(View.VISIBLE);
    }

    private void hidePastExpenses(List<Expense> allExpenses, String selectedDate) {

        for (Expense expense : allExpenses) {
            if (!expense.getTimeStamp().equals(selectedDate)) {

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        dateAdapter.startListening();
        expenseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        dateAdapter.stopListening();
        expenseAdapter.stopListening();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvCategory;
        private final TextView tvPrice;
        private final TextView tvTimeStamp;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
        }

        public void bind(Expense expense) {
            tvTitle.setText("Title: " + expense.getTitle());
            tvDescription.setText("Description: " + expense.getDescription());
            tvCategory.setText("Category: " + expense.getCategory());
            tvPrice.setText("Price: " + expense.getPrice());
            tvTimeStamp.setText(expense.getTimeStamp());
        }
    }
}
