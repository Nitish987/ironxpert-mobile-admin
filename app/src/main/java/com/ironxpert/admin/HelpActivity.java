package com.ironxpert.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ironxpert.admin.adapter.HelpRecyclerAdapter;
import com.ironxpert.admin.models.Help;

public class HelpActivity extends AppCompatActivity {
    private RecyclerView helpRV;
    private LinearLayout noHelpI;
    private ImageButton close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpRV = findViewById(R.id.help_RV);
        helpRV.setLayoutManager(new LinearLayoutManager(this));
        helpRV.setHasFixedSize(true);

        noHelpI = findViewById(R.id.no_help_i);
        close = findViewById(R.id.close);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = FirebaseFirestore.getInstance().collection("help").orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Help> options = new FirestoreRecyclerOptions.Builder<Help>().setQuery(query, Help.class).build();
        HelpRecyclerAdapter adapter = new HelpRecyclerAdapter(options, noHelpI);
        helpRV.setAdapter(adapter);
        adapter.startListening();

        close.setOnClickListener(view -> finish());
    }
}