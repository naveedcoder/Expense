package com.example.tabslayout;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Filter;

import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExpenseAdaptor extends FirebaseRecyclerAdapter<Expense, ExpenseAdaptor.ExpenseViewHolder> {
    Context parent;
    private List<Expense> expenses = new ArrayList<>();

    public ExpenseAdaptor(@NonNull FirebaseRecyclerOptions<Expense> options, Context context) {
        super(options);
        parent = context;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

        protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Expense model) {


        holder.tvTitle.setText("Title: "+model.getTitle());
        holder.tvDescription.setText("Description: "+model.getDescription());
        holder.tvCategory.setText("Category: "+model.getCategory());
        holder.tvPrice.setText("Price: "+model.getPrice());
        holder.tvTimeStamp.setText(model.getTimeStamp());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(parent)
                        .inflate(R.layout.alert_add_expense, null);


                TextInputEditText etTitle = view.findViewById(R.id.etTitle);
                TextInputEditText etDescription = view.findViewById(R.id.etDescription);
                TextInputEditText etPrice = view.findViewById(R.id.etPrice);
                TextInputEditText etCategory = view.findViewById(R.id.etCategory);
                TextView timeStamp = view.findViewById(R.id.timeStamp);

                etTitle.setText(model.getTitle());
                etDescription.setText(model.getDescription());
                etCategory.setText(model.getCategory());
                etPrice.setText(model.getPrice());


                SimpleDateFormat formattor = new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss");
                Date date = new Date();
                timeStamp.setText(formattor.format(date));
                AlertDialog.Builder updateExpense = new AlertDialog.Builder(parent)
                        .setTitle("Update Expense")
                        .setView(view)
                        .setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String Title = etTitle.getText().toString().trim();
                                String Description = etDescription.getText().toString().trim();
                                String Price = etPrice.getText().toString().trim();
                                String Category = etCategory.getText().toString().trim();
                                String Time = timeStamp.getText().toString().trim();

                                HashMap<String, Object> data = new HashMap<>();
                                data.put("Title", Title);
                                data.put("Description", Description);
                                data.put("Price", Price);
                                data.put("Category", Category);
                                data.put("timeStamp", timeStamp.getText().toString());

                                getRef(position)
                                        .updateChildren(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(parent, "Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(parent, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                updateExpense.create();
                updateExpense.show();
            }
        });


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder delete = new AlertDialog.Builder(parent)
                            .setTitle("Confirmation")
                            .setMessage("Do you really want to delete?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getSnapshots().getSnapshot(position)
                                            .getRef()
                                            .removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(parent, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(parent, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    delete.create().show();
                    return true;
                }
            });

    }


    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_expense_design, parent, false);
        return new ExpenseViewHolder(v);
    }


    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvCategory, tvPrice, tvTimeStamp;
        ImageView btnEdit;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}


