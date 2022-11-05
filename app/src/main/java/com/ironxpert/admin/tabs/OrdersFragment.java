package com.ironxpert.admin.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ironxpert.admin.R;
import com.ironxpert.admin.adapter.OrderRecyclerAdapter;
import com.ironxpert.admin.models.Order;

public class OrdersFragment extends Fragment {
    private RecyclerView ordersRV;
    private LinearLayout noOrdersI;
    private ChipGroup ordersFilter;

    private Query query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query = FirebaseFirestore.getInstance().collection("orders").whereEqualTo("orderState", 0).whereEqualTo("orderSuccess", true).orderBy("time", Query.Direction.ASCENDING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersRV = view.findViewById(R.id.order_rv);
        ordersRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ordersRV.setHasFixedSize(true);

        noOrdersI = view.findViewById(R.id.no_orders_i);
        ordersFilter = view.findViewById(R.id.orders_filter);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onStart() {
        super.onStart();
        ordersFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
            noOrdersI.setVisibility(View.VISIBLE);
            switch (group.getCheckedChipId()) {
                case R.id.ordered:
                    query = FirebaseFirestore.getInstance().collection("orders").whereEqualTo("orderState", 0).whereEqualTo("orderSuccess", true).orderBy("time", Query.Direction.ASCENDING);
                    break;

                case R.id.cooking:
                    query = FirebaseFirestore.getInstance().collection("orders").whereEqualTo("orderState", 1).whereEqualTo("orderSuccess", true).orderBy("time", Query.Direction.ASCENDING);
                    break;

                case R.id.dispatched:
                    query = FirebaseFirestore.getInstance().collection("orders").whereEqualTo("orderState", 2).whereEqualTo("orderSuccess", true).orderBy("time", Query.Direction.ASCENDING);
                    break;

                case R.id.on_way:
                    query = FirebaseFirestore.getInstance().collection("orders").whereEqualTo("orderState", 3).whereEqualTo("orderSuccess", true).orderBy("time", Query.Direction.ASCENDING);
                    break;

                case R.id.delivered:
                    query = FirebaseFirestore.getInstance().collection("orders").whereEqualTo("orderState", 4).whereEqualTo("orderSuccess", true).orderBy("time", Query.Direction.ASCENDING);
                    break;

                case R.id.canceled:
                    query = FirebaseFirestore.getInstance().collection("orders").whereEqualTo("orderSuccess", false).orderBy("time", Query.Direction.ASCENDING);
                    break;
            }

            setRecyclerAdapter();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>().setQuery(query, Order.class).build();
        OrderRecyclerAdapter adapter = new OrderRecyclerAdapter(options, noOrdersI);
        ordersRV.swapAdapter(adapter, true);
        adapter.startListening();
    }
}