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
import com.google.firebase.firestore.Query;
import com.ironxpert.admin.CreateDeliveryAgentActivity;
import com.ironxpert.admin.R;
import com.ironxpert.admin.adapter.AgentRecyclerAdapter;
import com.ironxpert.admin.common.db.Database;
import com.ironxpert.admin.models.Agent;

public class DispatcherFragment extends Fragment {
    private View view;
    private FloatingActionButton createDeliveryAgent;
    private RecyclerView agentsRV;
    private LinearLayout noDeliveryAgentI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dispatcher, container, false);

        createDeliveryAgent = view.findViewById(R.id.add_delivery_user_btn);
        noDeliveryAgentI = view.findViewById(R.id.no_delivery_i);

        agentsRV = view.findViewById(R.id.delivery_user_RV);
        agentsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        agentsRV.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        createDeliveryAgent.setOnClickListener(v -> {
            Agent agent = new Agent("", "", "", "", "");

            Intent intent = new Intent(view.getContext(), CreateDeliveryAgentActivity.class);
            intent.putExtra("AGENT", agent);
            intent.putExtra("NEW", true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Query query = Database.getInstance().collection("delivery_user");
        FirestoreRecyclerOptions<Agent> options = new FirestoreRecyclerOptions.Builder<Agent>().setQuery(query, Agent.class).build();
        AgentRecyclerAdapter adapter = new AgentRecyclerAdapter(options, noDeliveryAgentI);
        agentsRV.setAdapter(adapter);
        adapter.startListening();
    }
}