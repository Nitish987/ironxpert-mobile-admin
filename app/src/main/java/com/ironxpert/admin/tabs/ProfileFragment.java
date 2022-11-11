package com.ironxpert.admin.tabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ironxpert.admin.LoginActivity;
import com.ironxpert.admin.MyPhotoActivity;
import com.ironxpert.admin.R;
import com.ironxpert.admin.common.auth.Auth;
import com.ironxpert.admin.common.auth.AuthPreferences;
import com.ironxpert.admin.models.AdminUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private View view;
    private ImageView myPhoto;
    private TextView myNameTxt, emailTxt;
    private AppCompatButton logoutBtn, changePhotoBtn;
    private SwitchMaterial shopOpener, freeDelivery;

    private String photo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        myPhoto = view.findViewById(R.id.photo);
        myNameTxt = view.findViewById(R.id.my_name);
        emailTxt = view.findViewById(R.id.email);
        changePhotoBtn = view.findViewById(R.id.edit_photo);
        shopOpener = view.findViewById(R.id.shop_opener);
        freeDelivery = view.findViewById(R.id.free_delivery);
        logoutBtn = view.findViewById(R.id.logout);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection("admin").document(Objects.requireNonNull(Auth.getAuthUserUid())).get().addOnSuccessListener(documentSnapshot -> {
            AdminUser adminUser = documentSnapshot.toObject(AdminUser.class);

            assert adminUser != null;
            myNameTxt.setText(adminUser.getName());
            emailTxt.setText(adminUser.getEmail());
            if (adminUser.getPhoto() != null) {
                photo = documentSnapshot.get("photo", String.class);
                Glide.with(view.getContext()).load(photo).into(myPhoto);
            }
        });

//        FirebaseFirestore.getInstance().collection("shop").document("state").addSnapshotListener((value, error) -> {
//            if (value != null && value.exists()) {
//                boolean isOpen = value.get("open", Boolean.class);
//                shopOpener.setChecked(isOpen);
//
//                String o_ = "Shop Closed";
//                if (isOpen) {
//                    o_ = "Shop Open";
//                }
//                shopOpener.setText(o_);
//
//                boolean isDelivery = value.get("deliveryFree", Boolean.class);
//                freeDelivery.setChecked(isDelivery);
//
//                String d_ = "Paid Delivery";
//                if (isDelivery) {
//                    d_ = "Free Delivery";
//                }
//                freeDelivery.setText(d_);
//            }
//        });

        changePhotoBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), MyPhotoActivity.class);
            intent.putExtra("PHOTO", photo);
            startActivity(intent);
        });

        shopOpener.setOnCheckedChangeListener((compoundButton, b) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("open", b);
                FirebaseFirestore.getInstance().collection("shop").document("state").update(map).addOnFailureListener(e -> Toast.makeText(view.getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show());
        });

        freeDelivery.setOnCheckedChangeListener((compoundButton, b) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("deliveryFree", b);
                FirebaseFirestore.getInstance().collection("shop").document("state").update(map).addOnFailureListener(e -> Toast.makeText(view.getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show());
        });

        logoutBtn.setOnClickListener(view1 -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
            alert.setTitle("Logout");
            alert.setMessage("Are you sure, you want to logout.");
            alert.setCancelable(true);
            alert.setPositiveButton("Yes", (dialogInterface, i) -> {
                AuthPreferences preferences = new AuthPreferences(view.getContext());
                preferences.clear();
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            });
            alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            alert.show();
        });
    }
}