package com.ironxpert.admin.common.db;

import com.ironxpert.admin.R;
import com.ironxpert.admin.models.Service;

import java.util.ArrayList;
import java.util.List;

public class LaunderingService {
    public static final String SHOP = "emvQu3qThxF2n7pjouGd";
    public static List<Service> serviceList = new ArrayList<>();

    public static void setServices() {
        serviceList.add(new Service(0,"5 Star Dry Cleaning", "Premium Cleaning.", R.drawable.five_star_dry_clean));
        serviceList.add(new Service(1,"Dry Cleaning", "Cleaning with best quality.", R.drawable.dry_clean));
        serviceList.add(new Service(2,"Wash n Fold", "Washing and folding.", R.drawable.wash_n_fold));
        serviceList.add(new Service(3,"Wash n Iron", "Washing and iron.", R.drawable.wash_n_iron));
        serviceList.add(new Service(4,"Steam Iron", "Fresh and clean Steam Iron.", R.drawable.steam_iron));
        serviceList.add(new Service(5,"Shoe Wash", "Shoe Cleaning.", R.drawable.shoe_wash));
        serviceList.add(new Service(6,"Carpet Cleaning", "Cleaning Carpets with best quality.", R.drawable.carpet_clean));
    }

    public static List<Service> getServiceList() {
        if (serviceList.isEmpty()) {
            setServices();
        }

        return serviceList;
    }

    public static List<String> getServiceNameList() {
        if (serviceList.isEmpty()) {
            setServices();
        }

        List<String> list = new ArrayList<>();
        for (Service s: serviceList) {
            list.add(s.getName());
        }
        return list;
    }
}
