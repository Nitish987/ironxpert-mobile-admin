package com.ironxpert.admin.adapter;

import android.content.Intent;
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
import com.ironxpert.admin.ToppingActivity;
import com.ironxpert.admin.models.Topping;

public class ToppingItemRecyclerAdapter extends FirestoreRecyclerAdapter<Topping, ToppingItemRecyclerAdapter.ToppingItemHolder> {
    private final LinearLayout noToppingI;

    public ToppingItemRecyclerAdapter(FirestoreRecyclerOptions<Topping> options, LinearLayout noToppingI) {
        super(options);
        this.noToppingI = noToppingI;
    }

    @NonNull
    @Override
    public ToppingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ToppingItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topping, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ToppingItemHolder holder, int position, @NonNull Topping model) {
        noToppingI.setVisibility(View.INVISIBLE);

        holder.setPhoto(model.getPhoto());
        holder.setName(model.getName());
        holder.setPrice(model.getPrice());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ToppingActivity.class);
            intent.putExtra("TOPPING", model);
            intent.putExtra("NEW", false);
            view.getContext().startActivity(intent);
        });
    }

    public static class ToppingItemHolder extends RecyclerView.ViewHolder {
        private final ImageView photo;
        private final TextView name, price;

        public ToppingItemHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
        }

        public void setPhoto(String photo) {
            if (!photo.equals("")) Glide.with(this.photo.getContext()).load(photo).into(this.photo);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setPrice(int price) {
            String price_ = "\u20B9 " + price;
            this.price.setText(price_);
        }
    }
}
