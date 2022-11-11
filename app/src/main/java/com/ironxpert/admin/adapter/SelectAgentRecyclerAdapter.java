package com.ironxpert.admin.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ironxpert.admin.R;
import com.ironxpert.admin.models.DeliveryUser;

import java.util.HashMap;
import java.util.Map;

public class SelectAgentRecyclerAdapter extends FirestoreRecyclerAdapter<DeliveryUser, SelectAgentRecyclerAdapter.AgentHolder> {
    private final Activity activity;
    private final  String orderId;
    private final LinearLayout noDeliveryAgentI;

    public SelectAgentRecyclerAdapter(FirestoreRecyclerOptions<DeliveryUser> options, Activity activity, String orderId, LinearLayout noDeliveryAgentI) {
        super(options);
        this.activity = activity;
        this.orderId = orderId;
        this.noDeliveryAgentI = noDeliveryAgentI;
    }

    @NonNull
    @Override
    public SelectAgentRecyclerAdapter.AgentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectAgentRecyclerAdapter.AgentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agent, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull SelectAgentRecyclerAdapter.AgentHolder holder, int position, @NonNull DeliveryUser model) {
        noDeliveryAgentI.setVisibility(View.INVISIBLE);

        holder.setPhoto(model.getPhoto());
        holder.setName(model.getName());
        holder.setPhone(model.getPhone());

        holder.itemView.setOnClickListener(view -> {
            Map<String, Object> map = new HashMap<>();
            map.put("agentName", model.getName());
            map.put("agentPhone", model.getPhone());
            map.put("agentUid", model.getUid());
            FirebaseFirestore.getInstance().collection("orders").document(orderId).update(map).addOnSuccessListener(unused -> activity.finish()).addOnFailureListener(e -> Toast.makeText(view.getContext(), "Unable to allot delivery agent.", Toast.LENGTH_SHORT).show());
        });
    }

    public static class AgentHolder extends RecyclerView.ViewHolder {
        private final ImageView photo;
        private final TextView name, phone;

        public AgentHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }

        public void setPhoto(String photo) {
            if (!photo.equals(""))
                Glide.with(this.photo.getContext()).load(photo).into(this.photo);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setPhone(String phone) {
            this.phone.setText(phone);
        }
    }
}
