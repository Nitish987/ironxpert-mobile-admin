package com.ironxpert.admin.tabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ironxpert.admin.FoodActivity;
import com.ironxpert.admin.R;
import com.ironxpert.admin.adapter.FoodItemRecyclerAdapter;
import com.ironxpert.admin.models.Food;

public class FoodsFragment extends Fragment {
    private View view;
    private FloatingActionButton addFoodBtn;
    private RecyclerView foodsRV;
    private LinearLayout noFoodsI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_foods, container, false);

        addFoodBtn = view.findViewById(R.id.add_food_btn);
        noFoodsI = view.findViewById(R.id.no_food_i);

        foodsRV = view.findViewById(R.id.foods_RV);
        foodsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        foodsRV.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        addFoodBtn.setOnClickListener(v -> {
            Food food = new Food(true, "", "", 0, "", "", "", "", "", "", 0, 0.0);
            Intent intent = new Intent(view.getContext(), FoodActivity.class);
            intent.putExtra("FOOD", food);
            intent.putExtra("NEW", true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Query query = FirebaseFirestore.getInstance().collection("foods");
        FirestoreRecyclerOptions<Food> options = new FirestoreRecyclerOptions.Builder<Food>().setQuery(query, Food.class).build();
        FoodItemRecyclerAdapter adapter = new FoodItemRecyclerAdapter(options, noFoodsI);
        foodsRV.setAdapter(adapter);
        adapter.startListening();
    }
}