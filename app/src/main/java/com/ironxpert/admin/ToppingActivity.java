package com.ironxpert.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ironxpert.admin.common.db.Database;
import com.ironxpert.admin.models.Topping;
import com.ironxpert.admin.utils.Promise;
import com.ironxpert.admin.utils.Validator;

public class ToppingActivity extends AppCompatActivity {
    private ImageView photo;
    private EditText toppingName, includes, price;
    private Spinner available;
    private AppCompatButton save;
    private CircularProgressIndicator saveProgress;
    private ImageButton delete, close;

    private StorageReference reference;

    private Topping topping;
    private boolean NEW;
    private String photoUrl = null;
    private boolean isPhotoPicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topping);

        topping = (Topping) getIntent().getSerializableExtra("TOPPING");
        NEW = getIntent().getBooleanExtra("NEW", false);

        reference = FirebaseStorage.getInstance().getReference("toppings");

        photo = findViewById(R.id.photo);
        toppingName = findViewById(R.id.topping_name);
        includes = findViewById(R.id.food_includes);
        price = findViewById(R.id.price);
        available = findViewById(R.id.available);
        save = findViewById(R.id.save_btn);
        saveProgress = findViewById(R.id.save_progress);
        delete = findViewById(R.id.delete);
        close = findViewById(R.id.close);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (NEW) delete.setVisibility(View.GONE);

        photoUrl = topping.getPhoto();

        Glide.with(this).load(topping.getPhoto()).into(photo);
        toppingName.setText(topping.getName());
        includes.setText(topping.getFood_includes());

        if (topping.isAvailable()) available.setSelection(0);
        else available.setSelection(1);

        String p_ = Integer.toString(topping.getPrice());
        price.setText(p_);

        save.setOnClickListener(view -> {
            if (Validator.isEmpty(toppingName.getText().toString())) {
                toppingName.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(includes.getText().toString())) {
                includes.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(price.getText().toString())) {
                price.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(photoUrl)) {
                Toast.makeText(this, "Please add food photo.", Toast.LENGTH_SHORT).show();
                return;
            }

            save.setVisibility(View.INVISIBLE);

            if (NEW) {
                StorageReference storageReference = reference.child(Integer.toString((int) (Math.random() * 10000000)));
                storageReference.putFile(Uri.parse(photoUrl)).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(this::addNewData)).addOnProgressListener(snapshot -> saveProgress.setVisibility(View.VISIBLE)).addOnFailureListener(e -> {
                    saveProgress.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.VISIBLE);
                    Toast.makeText(ToppingActivity.this, "Unable to connect to server.", Toast.LENGTH_SHORT).show();
                });
            } else {
                if (isPhotoPicked) {
                    StorageReference storageReference = reference.child(Integer.toString((int) (Math.random() * 10000000)));
                    storageReference.putFile(Uri.parse(photoUrl)).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(this::updateOldData)).addOnProgressListener(snapshot -> saveProgress.setVisibility(View.VISIBLE)).addOnFailureListener(e -> {
                        saveProgress.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                        Toast.makeText(ToppingActivity.this, "Unable to connect to server.", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    updateOldData(Uri.parse(photoUrl));
                }
            }
        });

        ActivityResultLauncher<Intent> pickImgResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            try {
                Intent intent = result.getData();
                photoUrl = intent.getData().toString();
                Glide.with(this).load(photoUrl).into(photo);

                isPhotoPicked = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        photo.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            pickImgResult.launch(intent);
        });

        delete.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete Topping");
            alert.setMessage("Are you sure to delete this Topping item.");
            alert.setPositiveButton("Yes", (dialogInterface, i) -> Database.deleteTopping(this, topping.getTopping_id(), new Promise<String>() {
                @Override
                public void resolving(int progress, String msg) {}

                @Override
                public void resolved(String o) {
                    Toast.makeText(ToppingActivity.this, o, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void reject(String err) {
                    Toast.makeText(ToppingActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }));
            alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            alert.show();
        });

        close.setOnClickListener(view -> finish());
    }

    private void addNewData(Uri uri) {
        Database.addTopping(
                this,
                toppingName.getText().toString(),
                uri.toString(),
                includes.getText().toString(),
                available.getSelectedItemPosition() == 0,
                Integer.parseInt(price.getText().toString()),
                new Promise<String>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        saveProgress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void resolved(String o) {
                        Toast.makeText(ToppingActivity.this, o, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void reject(String err) {
                        saveProgress.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                        Toast.makeText(ToppingActivity.this, err, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void updateOldData(Uri uri) {
        Database.editTopping(
                this,
                topping.getTopping_id(),
                toppingName.getText().toString(),
                uri.toString(),
                includes.getText().toString(),
                available.getSelectedItemPosition() == 0,
                Integer.parseInt(price.getText().toString()),
                new Promise<String>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        saveProgress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void resolved(String o) {
                        Toast.makeText(ToppingActivity.this, o, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void reject(String err) {
                        saveProgress.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                        Toast.makeText(ToppingActivity.this, err, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}