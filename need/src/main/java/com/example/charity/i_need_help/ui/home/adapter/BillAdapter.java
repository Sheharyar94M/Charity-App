package com.example.charity.i_need_help.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charity.Model.BillPayment;
import com.example.charity.R;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.AdapterViewHolder> {

    private ArrayList<BillPayment> list;

    public void setlist(ArrayList<BillPayment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posted_job_list_layout,parent,false);

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {

        holder.reference.setText(list.get(position).getReference_no());
        holder.due_date.setText(list.get(position).getDue_date());
        holder.amount.setText(list.get(position).getTotal_amount());

    }

    @Override
    public int getItemCount() {

        if (list != null)
            return list.size();
        else
            return 0;
    }

    static class AdapterViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView reference;
        AppCompatTextView due_date;
        AppCompatTextView amount;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            reference = itemView.findViewById(R.id.reference_value);
            due_date = itemView.findViewById(R.id.due_date_value);
            amount = itemView.findViewById(R.id.TotalPrice_value);
        }
    }
}
