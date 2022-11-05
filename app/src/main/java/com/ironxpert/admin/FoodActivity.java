package com.ironxpert.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ironxpert.admin.common.db.Database;
import com.ironxpert.admin.models.Food;
import com.ironxpert.admin.utils.Promise;
import com.ironxpert.admin.utils.Validator;

public class FoodActivity extends AppCompatActivity {
    private ImageView photo;
    private EditText foodName, description, includes, ingredients, price, discount;
    private Spinner foodType, available;
    private AppCompatButton save;
    private CircularProgressIndicator saveProgress;
    private ImageButton delete, close;
    private TextView payablePrice;

    private StorageReference reference;

    private Food food;
    private boolean NEW;
    private String photoUrl = null;
    private boolean isPhotoPicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        food = (Food) getIntent().getSerializableExtra("FOOD");
        NEW = getIntent().getBooleanExtra("NEW", false);

        reference = FirebaseStorage.getInstance().getReference("foods");

        photo = findViewById(R.id.photo);
        foodName = findViewById(R.id.food_name);
        description = findViewById(R.id.description);
        includes = findViewById(R.id.food_includes);
        ingredients = findViewById(R.id.ingredients);
        price = findViewById(R.id.price);
        discount = findViewById(R.id.discount);
        foodType = findViewById(R.id.food_type);
        available = findViewById(R.id.available);
        save = findViewById(R.id.save_btn);
        saveProgress = findViewById(R.id.save_progress);
        delete = findViewById(R.id.delete);
        close = findViewById(R.id.close);
        payablePrice = findViewById(R.id.payable_price);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (NEW) delete.setVisibility(View.GONE);

        photoUrl = food.getPhoto();

        Glide.with(this).load(food.getPhoto()).into(photo);
        foodName.setText(food.getName());
        description.setText(food.getDescription());
        includes.setText(food.getFood_includes());
        ingredients.setText(food.getIngredients());

        switch (food.getFood_type()) {
            case "breakfast": foodType.setSelection(0); break;
            case "lunch": foodType.setSelection(1); break;
            case "dinner": foodType.setSelection(2); break;
        }

        if (food.isAvailable()) available.setSelection(0);
        else available.setSelection(1);

        String p_ = Integer.toString(food.getPrice());
        price.setText(p_);

        String d_ = Integer.toString(food.getDiscount());
        discount.setText(d_);

        String pp_ = "Payable Price : " + "\u20B9 " + getPayablePrice(food.getPrice(), food.getDiscount());
        payablePrice.setText(pp_);

        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!price.getText().toString().equals("") && !discount.getText().toString().equals("")) {
                    String pp_ = "Payable Price : " + "\u20B9 " + getPayablePrice(Integer.parseInt(price.getText().toString()), Integer.parseInt(discount.getText().toString()));
                    payablePrice.setText(pp_);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        save.setOnClickListener(view -> {
            if (Validator.isEmpty(foodName.getText().toString())) {
                foodName.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(description.getText().toString())) {
                description.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(includes.getText().toString())) {
                includes.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(ingredients.getText().toString())) {
                ingredients.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(price.getText().toString())) {
                price.setError("Field Required");
                return;
            }

            if (Validator.isEmpty(discount.getText().toString())) {
                discount.setError("Field Required");
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
                    Toast.makeText(FoodActivity.this, "Unable to connect to server.", Toast.LENGTH_SHORT).show();
                });
            } else {
                if (isPhotoPicked) {
                    StorageReference storageReference = reference.child(Integer.toString((int) (Math.random() * 10000000)));
                    storageReference.putFile(Uri.parse(photoUrl)).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(this::updateOldData)).addOnProgressListener(snapshot -> saveProgress.setVisibility(View.VISIBLE)).addOnFailureListener(e -> {
                        saveProgress.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                        Toast.makeText(FoodActivity.this, "Unable to connect to server.", Toast.LENGTH_SHORT).show();
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
            alert.setTitle("Delete Food");
            alert.setMessage("Are you sure to delete this food item.");
            alert.setPositiveButton("Yes", (dialogInterface, i) -> Database.deleteFood(this, food.getFood_id(), new Promise<String>() {
                @Override
                public void resolving(int progress, String msg) {}

                @Override
                public void resolved(String o) {
                    Toast.makeText(FoodActivity.this, o, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void reject(String err) {
                    Toast.makeText(FoodActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }));
            alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            alert.show();
        });

        close.setOnClickListener(view -> finish());
    }

    private void addNewData(Uri uri) {
        Database.addFood(
                this,
                foodName.getText().toString(),
                uri.toString(),
                description.getText().toString(),
                includes.getText().toString(),
                ingredients.getText().toString(),
                foodType.getSelectedItem().toString(),
                available.getSelectedItemPosition() == 0,
                Integer.parseInt(price.getText().toString()),
                Integer.parseInt(discount.getText().toString()),
                new Promise<String>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        saveProgress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void resolved(String o) {
                        Toast.makeText(FoodActivity.this, o, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void reject(String err) {
                        saveProgress.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                        Toast.makeText(FoodActivity.this, err, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void updateOldData(Uri uri) {
        Database.editFood(
                this,
                food.getFood_id(),
                foodName.getText().toString(),
                uri.toString(),
                description.getText().toString(),
                includes.getText().toString(),
                ingredients.getText().toString(),
                foodType.getSelectedItem().toString(),
                available.getSelectedItemPosition() == 0,
                Integer.parseInt(price.getText().toString()),
                Integer.parseInt(discount.getText().toString()),
                new Promise<String>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        saveProgress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void resolved(String o) {
                        Toast.makeText(FoodActivity.this, o, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void reject(String err) {
                        saveProgress.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                        Toast.makeText(FoodActivity.this, err, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private int getPayablePrice(int price, int discount) {
        if (discount != 0) {
            price = price - Math.round((float) (discount * price) / 100);
        }
        return price;
    }
}