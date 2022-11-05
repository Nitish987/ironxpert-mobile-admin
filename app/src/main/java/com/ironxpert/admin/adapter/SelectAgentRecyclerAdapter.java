package com.ironxpert.admin.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ironxpert.admin.R;
import com.ironxpert.admin.models.Agent;

import java.util.HashMap;
import java.util.Map;

public class SelectAgentRecyclerAdapter extends FirestoreRecyclerAdapter<Agent, AgentRecyclerAdapter.AgentHolder> {
    private final Activity activity;
    private final  String orderId;
    private final LinearLayout noDeliveryAgentI;

    public SelectAgentRecyclerAdapter(FirestoreRecyclerOptions<Agent> options, Activity activity, String orderId, LinearLayout noDeliveryAgentI) {
        super(options);
        this.activity = activity;
        this.orderId = orderId;
        this.noDeliveryAgentI = noDeliveryAgentI;
    }

    @NonNull
    @Override
    public AgentRecyclerAdapter.AgentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AgentRecyclerAdapter.AgentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agent, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull AgentRecyclerAdapter.AgentHolder holder, int position, @NonNull Agent model) {
        noDeliveryAgentI.setVisibility(View.INVISIBLE);

        holder.setPhoto(model.getPhoto());
        holder.setName(model.getName());
        holder.setPhone(model.getPhone());

        holder.itemView.setOnClickListener(view -> {
            Map<String, Object> map = new HashMap<>();
            map.put("agentName", model.getName());
            map.put("agentPhone", "+91" + model.getPhone());
            map.put("agentUid", model.getUid());
            FirebaseFirestore.getInstance().collection("orders").document(orderId).update(map).addOnSuccessListener(unused -> activity.finish()).addOnFailureListener(e -> Toast.makeText(view.getContext(), "Unable to allot delivery agent.", Toast.LENGTH_SHORT).show());
        });
    }
}
