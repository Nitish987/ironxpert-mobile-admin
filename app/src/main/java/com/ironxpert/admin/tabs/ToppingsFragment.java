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
import com.ironxpert.admin.R;
import com.ironxpert.admin.ToppingActivity;
import com.ironxpert.admin.adapter.ToppingItemRecyclerAdapter;
import com.ironxpert.admin.models.Topping;

public class ToppingsFragment extends Fragment {
    private View view;
    private FloatingActionButton addToppingBtn;
    private RecyclerView toppingsRV;
    private LinearLayout noToppingI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_toppings, container, false);

        addToppingBtn = view.findViewById(R.id.add_topping_btn);
        noToppingI = view.findViewById(R.id.no_topping_i);

        toppingsRV = view.findViewById(R.id.toppings_RV);
        toppingsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        toppingsRV.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        addToppingBtn.setOnClickListener(v -> {
            Topping topping = new Topping(true, "", "", "", "", 0,0, "");
            Intent intent = new Intent(view.getContext(), ToppingActivity.class);
            intent.putExtra("TOPPING", topping);
            intent.putExtra("NEW", true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Query query = FirebaseFirestore.getInstance().collection("toppings");
        FirestoreRecyclerOptions<Topping> options = new FirestoreRecyclerOptions.Builder<Topping>().setQuery(query, Topping.class).build();
        ToppingItemRecyclerAdapter adapter = new ToppingItemRecyclerAdapter(options, noToppingI);
        toppingsRV.setAdapter(adapter);
        adapter.startListening();
    }
}