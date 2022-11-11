package com.ironxpert.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.ironxpert.admin.R;
import com.ironxpert.admin.models.DeliveryUser;

public class AgentRecyclerAdapter extends FirestoreRecyclerAdapter<DeliveryUser, AgentRecyclerAdapter.AgentHolder> {
    private final LinearLayout noDeliveryAgentI;

    public AgentRecyclerAdapter(FirestoreRecyclerOptions<DeliveryUser> options, LinearLayout noDeliveryAgentI) {
        super(options);
        this.noDeliveryAgentI = noDeliveryAgentI;
    }

    @NonNull
    @Override
    public AgentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AgentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agent, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull AgentHolder holder, int position, @NonNull DeliveryUser model) {
        noDeliveryAgentI.setVisibility(View.INVISIBLE);

        holder.setPhoto(model.getPhoto());
        holder.setName(model.getName());
        holder.setPhone(model.getPhone());
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
