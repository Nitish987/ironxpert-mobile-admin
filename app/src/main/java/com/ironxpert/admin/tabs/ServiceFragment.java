package com.ironxpert.admin.tabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ironxpert.admin.R;
import com.ironxpert.admin.ServiceItemDetailActivity;
import com.ironxpert.admin.adapter.ServiceRecyclerAdapter;
import com.ironxpert.admin.common.db.LaunderingService;
import com.ironxpert.admin.models.Service;
import com.ironxpert.admin.models.ServiceItem;

import java.util.List;

public class ServiceFragment extends Fragment {
    private View view;
    private RecyclerView serviceRv;
    private FloatingActionButton addServiceItemBtn;

    private List<Service> serviceList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceList = LaunderingService.getServiceList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, container, false);

        addServiceItemBtn = view.findViewById(R.id.add_service_item_btn);
        serviceRv = view.findViewById(R.id.services_rv);
        serviceRv.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ServiceRecyclerAdapter serviceAdapter = new ServiceRecyclerAdapter(serviceList);
        serviceRv.setAdapter(serviceAdapter);

        addServiceItemBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ServiceItemDetailActivity.class);
            ServiceItem item = new ServiceItem(true, "", "", 0,"", "","",0);
            intent.putExtra("ITEM", item);
            intent.putExtra("NEW", true);
            startActivity(intent);
        });
    }
}