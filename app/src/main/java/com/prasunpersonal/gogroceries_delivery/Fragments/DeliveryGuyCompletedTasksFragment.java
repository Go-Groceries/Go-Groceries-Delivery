package com.prasunpersonal.gogroceries_delivery.Fragments;

import static com.prasunpersonal.gogroceries_delivery.App.ME;
import static com.prasunpersonal.gogroceries_delivery.Models.ModelOrder.DELIVERED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prasunpersonal.gogroceries_delivery.Activities.DeliveryGuyOrderDetailsActivity;
import com.prasunpersonal.gogroceries_delivery.Adapters.AdapterOrder;
import com.prasunpersonal.gogroceries_delivery.Models.ModelOrder;
import com.prasunpersonal.gogroceries_delivery.databinding.FragmentDeliveryGuyCompletedTasksBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class DeliveryGuyCompletedTasksFragment extends Fragment {
    FragmentDeliveryGuyCompletedTasksBinding binding;
    Context context;
    ArrayList<ModelOrder> orders;

    public DeliveryGuyCompletedTasksFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeliveryGuyCompletedTasksBinding.inflate(inflater, container, false);
        context = binding.getRoot().getContext();

        orders = new ArrayList<>();
        binding.completedDeliveries.setLayoutManager(new LinearLayoutManager(context));
        binding.completedDeliveries.setAdapter(new AdapterOrder(context, orders, (order, position) -> startActivity(new Intent(context, DeliveryGuyOrderDetailsActivity.class).putExtra("ORDER_ID", order.getOrderId()))));

        loadOrders();
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadOrders() {
        FirebaseFirestore.getInstance().collection("Orders").whereEqualTo("deliveryGuyId", ME.getUid()).whereEqualTo("orderStatus", DELIVERED).orderBy("orderTime").addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (value != null) {
                orders.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    orders.add(doc.toObject(ModelOrder.class));
                }
                Objects.requireNonNull(binding.completedDeliveries.getAdapter()).notifyDataSetChanged();
            }
        });
    }
}