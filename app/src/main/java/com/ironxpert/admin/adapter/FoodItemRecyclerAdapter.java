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
import com.ironxpert.admin.models.Food;

public class FoodItemRecyclerAdapter extends FirestoreRecyclerAdapter<Food, FoodItemRecyclerAdapter.FoodItemHolder> {
    private final LinearLayout noFoodsI;

    public FoodItemRecyclerAdapter(FirestoreRecyclerOptions<Food> options, LinearLayout noFoodsI) {
        super(options);
        this.noFoodsI = noFoodsI;
    }

    @NonNull
    @Override
    public FoodItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodItemHolder holder, int position, @NonNull Food model) {
        noFoodsI.setVisibility(View.INVISIBLE);

        holder.setPhoto(model.getPhoto());
        holder.setName(model.getName());
        holder.setType(model.getFood_type());
        holder.setPrice(model.getPrice(), model.getDiscount());
        holder.setRating(model.getRating());

        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(view.getContext(), FoodActivity.class);
//            intent.putExtra("FOOD", model);
//            intent.putExtra("NEW", false);
//            view.getContext().startActivity(intent);
        });
    }

    public static class FoodItemHolder extends RecyclerView.ViewHolder {
        private final ImageView photo;
        private final TextView name, type, price, rating, discount;

        public FoodItemHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.food_type);
            price = itemView.findViewById(R.id.price);
            rating = itemView.findViewById(R.id.rating);
            discount = itemView.findViewById(R.id.discount);
        }

        public void setPhoto(String photo) {
            if (!photo.equals("")) Glide.with(this.photo.getContext()).load(photo).into(this.photo);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setType(String type) {
            this.type.setText(type);
        }

        public void setPrice(int price, int discount) {
            if (discount == 0) {
                this.discount.setVisibility(View.INVISIBLE);
            } else {
                this.discount.setVisibility(View.VISIBLE);
                String discount_ = discount + "% OFF";
                this.discount.setText(discount_);
            }

            String price_ = "\u20B9 " + price;
            this.price.setText(price_);
        }

        public void setRating(double rating) {
            String rate = Double.toString(rating);
            this.rating.setText(rate);
        }
    }
}
