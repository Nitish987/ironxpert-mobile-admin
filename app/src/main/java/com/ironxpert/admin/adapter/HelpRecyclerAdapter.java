package com.ironxpert.admin.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ironxpert.admin.R;
import com.ironxpert.admin.models.Help;
import com.ironxpert.admin.utils.DateParser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HelpRecyclerAdapter extends FirestoreRecyclerAdapter<Help, HelpRecyclerAdapter.HelpHolder> {
    private final LinearLayout noHelpI;

    public HelpRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Help> options, LinearLayout noHelpI) {
        super(options);
        this.noHelpI = noHelpI;
    }

    @Override
    protected void onBindViewHolder(@NonNull HelpHolder holder, int position, @NonNull Help model) {
        noHelpI.setVisibility(View.GONE);

        holder.setEmail(model.getEmail());
        holder.setSubject(model.getType());
        holder.setMessage(model.getMessage());
        holder.setTime(model.getTime());

        if (model.isRead()) {
            holder.email.setTextColor(holder.itemView.getContext().getColor(R.color.green));
        } else {
            holder.email.setTextColor(holder.itemView.getContext().getColor(R.color.red));
        }

        holder.itemView.setOnClickListener(view -> {
            Map<String, Object> map = new HashMap<>();
            map.put("read", true);
            FirebaseFirestore.getInstance().collection("help").document(model.getHelp_id()).update(map).addOnSuccessListener(unused -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, model.getEmail());
                intent.putExtra(Intent.EXTRA_SUBJECT, "Reply : " + model.getType());
                intent.putExtra(Intent.EXTRA_TEXT, model.getMessage() + "\n\n" + "Reply : ");
                view.getContext().startActivity(intent);
            });
        });
    }

    @NonNull
    @Override
    public HelpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HelpHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help, parent, false));
    }

    public static class HelpHolder extends RecyclerView.ViewHolder {
        private final TextView email, subject, message, time;

        public HelpHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.email);
            subject = itemView.findViewById(R.id.subject);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
        }

        public void setEmail(String e) {
            email.setText(e);
        }

        public void setSubject(String s) {
            subject.setText(s);
        }

        public void setMessage(String m) {
            message.setText(m);
        }

        public void setTime(long t) {
            String t_ = DateParser.parse(new Date(t));
            time.setText(t_);
        }
    }
}
