package com.example.ruralhealthadmin;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ItemHolder> {



    @NonNull
    @Override
    public EmployeeAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public static class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
