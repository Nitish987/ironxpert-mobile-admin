package com.ironxpert.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.ironxpert.admin.common.db.Database;
import com.ironxpert.admin.models.Agent;
import com.ironxpert.admin.utils.Promise;
import com.ironxpert.admin.utils.Validator;

public class CreateDeliveryAgentActivity extends AppCompatActivity {
    private EditText name, phone, email;
    private AppCompatButton save;
    private CircularProgressIndicator saveProgress;
    private ImageButton delete, close;

    private Agent agent;
    private boolean NEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_delivery_agent);

        agent = (Agent) getIntent().getSerializableExtra("AGENT");
        NEW = getIntent().getBooleanExtra("NEW", false);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        save = findViewById(R.id.save_btn);
        saveProgress = findViewById(R.id.save_progress);
        delete = findViewById(R.id.delete);
        close = findViewById(R.id.close);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (NEW) delete.setVisibility(View.GONE);
        else {
            name.setEnabled(false);
            phone.setEnabled(false);
            email.setEnabled(false);

            save.setVisibility(View.INVISIBLE);
        }

        name.setText(agent.getName());
        phone.setText(agent.getPhone());
        email.setText(agent.getEmail());

        save.setOnClickListener(view -> {
            if (Validator.isEmpty(name.getText().toString())) {
                name.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(phone.getText().toString())) {
                phone.setError("Field Required");
                return;
            }

            if (!Validator.isEqualLength(phone.getText().toString(), 10)) {
                phone.setError("Invalid Phone Number.");
                return;
            }

            if (!Validator.isEmail(email.getText().toString())) {
                email.setError("Invalid Email.");
                return;
            }

            if (Validator.isEmpty(email.getText().toString())) {
                email.setError("Field Required");
                return;
            }

            save.setVisibility(View.INVISIBLE);
            if (NEW) {
                Database.OthersAuth.createDeliveryAgent(
                        this,
                        name.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString(),
                        new Promise<String>() {
                            @Override
                            public void resolving(int progress, String msg) {
                                saveProgress.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void resolved(String o) {
                                saveProgress.setVisibility(View.INVISIBLE);
                                Toast.makeText(CreateDeliveryAgentActivity.this, o, Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void reject(String err) {
                                save.setVisibility(View.VISIBLE);
                                Toast.makeText(CreateDeliveryAgentActivity.this, err, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                );
            }
        });

        delete.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete Delivery User");
            alert.setMessage("Are you sure to delete this delivery user account.");
            alert.setPositiveButton("Yes", (dialogInterface, i) -> Database.OthersAuth.deleteDeliveryAgent(this, agent.getUid(), new Promise<String>() {
                @Override
                public void resolving(int progress, String msg) {}

                @Override
                public void resolved(String o) {
                    Toast.makeText(CreateDeliveryAgentActivity.this, o, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void reject(String err) {
                    Toast.makeText(CreateDeliveryAgentActivity.this, err, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }));
            alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            alert.show();
        });

        close.setOnClickListener(view -> finish());
    }
}