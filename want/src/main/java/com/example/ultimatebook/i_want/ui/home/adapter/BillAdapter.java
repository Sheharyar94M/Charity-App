package com.example.ultimatebook.i_want.ui.home.adapter;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ultimatebook.R;
import com.example.ultimatebook.model.BillTobePaid;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.AdapterViewHolder> {

    private ArrayList<BillTobePaid> list;
    private final Context context;

    public BillAdapter(Context context) {
        this.context = context;
    }

    public void setlist(ArrayList<BillTobePaid> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.needs_to_fil_rv, parent, false);

        return new AdapterViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {

        holder.reference.setText(list.get(position).getReference_no());
        holder.due_date.setText(list.get(position).getDue_date());
        holder.amount.setText(list.get(position).getTotal_amount());

        holder.verification_link.setOnLongClickListener(v -> {

            ClipboardManager myClipboard = (ClipboardManager) v.getContext().getSystemService(CLIPBOARD_SERVICE);

            ClipData myClip = ClipData.newPlainText("link_verify", holder.verification_link.getText().toString().trim());
            assert myClipboard != null;
            myClipboard.setPrimaryClip(myClip);

            Toast.makeText(context, "Link copied to board!", Toast.LENGTH_SHORT).show();

            return true;

        });


        holder.pay_button.setOnClickListener(v -> {

//            Toast.makeText(context, "started!", Toast.LENGTH_SHORT).show();

            Intent launchIntent = v.getContext().getPackageManager().getLaunchIntentForPackage("pk.com.telenor.phoenix");
            if (launchIntent != null) {
                v.getContext().startActivity(launchIntent);         //null pointer check in case package name was not found
            }else{

                Toast.makeText(context, "No app found!", Toast.LENGTH_SHORT).show();

            }

        });

    }

    @Override
    public int getItemCount() {

        if (list != null)
            return list.size();
        else
            return 0;
    }

    static class AdapterViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView reference;
        AppCompatTextView due_date;
        AppCompatTextView amount;
        AppCompatTextView verification_link;
        AppCompatButton pay_button;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            reference = itemView.findViewById(R.id.reference_value);
            due_date = itemView.findViewById(R.id.due_date_value);
            amount = itemView.findViewById(R.id.TotalPrice_value);
            pay_button = itemView.findViewById(R.id.payment_button);
            verification_link = itemView.findViewById(R.id.link_value);
        }
    }
}
