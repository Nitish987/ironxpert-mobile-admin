package com.ironxpert.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.ironxpert.admin.common.auth.Auth;
import com.ironxpert.admin.common.db.Database;
import com.ironxpert.admin.common.db.LaunderingService;
import com.ironxpert.admin.models.ServiceItem;
import com.ironxpert.admin.utils.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceItemDetailActivity extends AppCompatActivity {
    private EditText eItemName, ePrice, eDiscount;
    private Spinner sCategory, sItemAvailable, sService;
    private TextView tPayablePrice;
    private AppCompatButton bSave;
    private ImageButton bDelete, bClose;

    private ServiceItem item;
    private boolean NEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_item_detail);

        item = (ServiceItem) getIntent().getSerializableExtra("ITEM");
        NEW = getIntent().getBooleanExtra("NEW", false);
        List<String> serviceList = LaunderingService.getServiceNameList();

        eItemName = findViewById(R.id.item_name);
        ePrice = findViewById(R.id.price);
        eDiscount = findViewById(R.id.discount);
        sCategory = findViewById(R.id.category);
        sItemAvailable = findViewById(R.id.available);
        tPayablePrice = findViewById(R.id.payable_price);
        bSave = findViewById(R.id.save_btn);
        bDelete = findViewById(R.id.delete);
        bClose = findViewById(R.id.close);
        sService = findViewById(R.id.service);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, serviceList);
        sService.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (NEW) bDelete.setVisibility(View.GONE);

        eItemName.setText(item.getName());

        switch (item.getCategory()) {
            case "Apparels": sCategory.setSelection(0); break;
            case "Linens": sCategory.setSelection(1); break;
            case "Premium/Wedding Wear": sCategory.setSelection(2); break;
            case "Woollens/Silks": sCategory.setSelection(3); break;
            case "Wash And Fold Laundry": sCategory.setSelection(4); break;
            case "Wash And Iron Laundry": sCategory.setSelection(5); break;
            case "Shoe Laundry": sCategory.setSelection(6); break;
            case "Carpet Care": sCategory.setSelection(7); break;
        }

        sService.setSelection(item.getService());

        if (item.isAvailable()) sItemAvailable.setSelection(0);
        else sCategory.setSelection(1);

        String p_ = Integer.toString(item.getPrice());
        ePrice.setText(p_);

        String d_ = Integer.toString(item.getDiscount());
        eDiscount.setText(d_);

        String pp_ = "Payable Price : " + "\u20B9 " + getPayablePrice(item.getPrice(), item.getDiscount());
        tPayablePrice.setText(pp_);

        eDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!ePrice.getText().toString().equals("") && !eDiscount.getText().toString().equals("")) {
                    String pp_ = "Payable Price : " + "\u20B9 " + getPayablePrice(Integer.parseInt(ePrice.getText().toString()), Integer.parseInt(eDiscount.getText().toString()));
                    tPayablePrice.setText(pp_);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bSave.setOnClickListener(view -> {
            if (Validator.isEmpty(eItemName.getText().toString())) {
                eItemName.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(ePrice.getText().toString())) {
                ePrice.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(eDiscount.getText().toString())) {
                eDiscount.setError("Field Required");
                return;
            }

            bSave.setVisibility(View.INVISIBLE);

            CollectionReference reference = Database.getInstance().collection("shop").document(LaunderingService.SHOP).collection("items");

            if (NEW) {
                DocumentReference documentReference = reference.document();

                ServiceItem item = new ServiceItem(
                        sItemAvailable.getSelectedItemPosition() == 0,
                        Auth.getAuthUserUid(),
                        sCategory.getSelectedItem().toString(),
                        Integer.parseInt(eDiscount.getText().toString()),
                        documentReference.getId(),
                        eItemName.getText().toString(),
                        null,
                        Integer.parseInt(ePrice.getText().toString()),
                        sService.getSelectedItemPosition()
                );
                documentReference.set(item).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Item Added.", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    bSave.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Unable to add Item.", Toast.LENGTH_SHORT).show();
                });
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("available", sItemAvailable.getSelectedItemPosition() == 0);
                map.put("by", Auth.getAuthUserUid());
                map.put("category", sCategory.getSelectedItem().toString());
                map.put("discount", Integer.parseInt(eDiscount.getText().toString()));
                map.put("id", reference.getId());
                map.put("name", eItemName.getText().toString());
                map.put("photo", null);
                map.put("price", Integer.parseInt(ePrice.getText().toString()));
                map.put("service", sService.getSelectedItemPosition());

                reference.document(this.item.getId()).update(map).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Item Updated.", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    bSave.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Unable to add Item.", Toast.LENGTH_SHORT).show();
                });
            }
        });

        bDelete.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete Item");
            alert.setMessage("Are you sure to delete this item.");
            alert.setPositiveButton("Yes", (dialogInterface, i) -> {
                Database.getInstance().collection("shop").document(LaunderingService.SHOP).collection("items").document(item.getId()).delete().addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Item Deleted.", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Unable to add Item.", Toast.LENGTH_SHORT).show();
                });
            });
            alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            alert.show();
        });

        bClose.setOnClickListener(view -> finish());
    }

    private int getPayablePrice(int price, int discount) {
        if (discount != 0) {
            price = price - Math.round((float) (discount * price) / 100);
        }
        return price;
    }
}