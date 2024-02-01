package com.example.tabslayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class CategoryAdaptor extends FirebaseRecyclerAdapter<Category, CategoryAdaptor.CategoryViewHolder> {
    Context parent;

    public CategoryAdaptor(@NonNull FirebaseRecyclerOptions<Category> options, Context context) {
        super(options);
        parent = context;
    }


    protected void onBindViewHolder(@NonNull CategoryAdaptor.CategoryViewHolder holder, int position, @NonNull Category model) {

        String categoryName = model.getCategory();

        holder.tvCategoryName.setText(categoryName);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(parent, ExpenseActivity.class);
                intent.putExtra("selectedCategory", categoryName);
                parent.startActivity(intent);

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


    public CategoryAdaptor.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_design, parent, false);
        return new CategoryAdaptor.CategoryViewHolder(v);
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);


        }
    }
}
