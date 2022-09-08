package com.prasunpersonal.gogroceries_delivery.Activities;

import static com.prasunpersonal.gogroceries_delivery.Models.ModelOrder.DELIVERED;
import static com.prasunpersonal.gogroceries_delivery.Models.ModelOrder.MONEY_RECEIVED;
import static com.prasunpersonal.gogroceries_delivery.Models.ModelOrder.ORDER_PACKED;
import static com.prasunpersonal.gogroceries_delivery.Models.ModelOrder.OUT_FOR_DELIVERY;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prasunpersonal.gogroceries_delivery.Models.ModelCartItem;
import com.prasunpersonal.gogroceries_delivery.Models.ModelCustomer;
import com.prasunpersonal.gogroceries_delivery.Models.ModelOrder;
import com.prasunpersonal.gogroceries_delivery.Models.ModelShop;
import com.prasunpersonal.gogroceries_delivery.databinding.ActivityDeliveryGuyOrderDetailsBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DeliveryGuyOrderDetailsActivity extends AppCompatActivity {
    ActivityDeliveryGuyOrderDetailsBinding binding;
    String orderID;
    ModelOrder order;
    ModelCustomer customer;
    ModelShop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryGuyOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.orderDetailsToolbar);

        binding.orderDetailsToolbar.setNavigationOnClickListener(v -> finish());

        orderID = getIntent().getStringExtra("ORDER_ID");

        binding.callShop.setOnClickListener(v -> {
            if (!binding.shopPhone.getText().toString().trim().isEmpty()) {
                dialPhone(binding.shopPhone.getText().toString().trim());
            }
        });

        binding.mapShop.setOnClickListener(v -> {
            if (shop != null) {
                openMap(shop.getLatitude(), shop.getLongitude());
            }
        });

        binding.callCustomer.setOnClickListener(v -> {
            if (!binding.customerPhone.getText().toString().trim().isEmpty()) {
                dialPhone(binding.customerPhone.getText().toString().trim());
            }
        });

        binding.mapCustomer.setOnClickListener(v -> {
            if (order != null) {
                openMap(order.getLatitude(), order.getLongitude());
            }
        });

        binding.deliveredBtn.setOnClickListener(v -> {
            if (order != null) {
                if (binding.secretCode.getText().toString().trim().equals(order.getSecretCode())) {
                    FirebaseFirestore.getInstance().collection("Orders").document(order.getOrderId()).update("orderStatus", DELIVERED);
                } else {
                    binding.secretCode.setError("The secret code doesn't match!");
                }
            } else {
                Toast.makeText(this, "Wait till the order is fully loaded.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.orderPickedUp.setOnClickListener(v -> {
            if (order != null) {
                FirebaseFirestore.getInstance().collection("Orders").document(order.getOrderId()).update("orderStatus", OUT_FOR_DELIVERY);
            } else {
                Toast.makeText(this, "Wait till the order is fully loaded.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.moneyReceived.setOnClickListener(v -> {
            if (order != null) {
                FirebaseFirestore.getInstance().collection("Orders").document(order.getOrderId()).update("orderStatus", MONEY_RECEIVED);
            } else {
                Toast.makeText(this, "Wait till the order is fully loaded.", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseFirestore.getInstance().collection("Orders").document(orderID).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            if (value != null && value.exists()) {
                order = value.toObject(ModelOrder.class);
                assert order != null;

                FirebaseFirestore.getInstance().collection("Customers").document(order.getCustomerId()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        customer = task.getResult().toObject(ModelCustomer.class);
                        assert customer != null;
                        binding.customerName.setText(customer.getName());
                        binding.customerPhone.setText(customer.getPhone());
                        binding.customerAddress.setText(order.getDeliveryAddress());

                        if (shop != null) {
                            binding.orderTitle.setText(String.format(Locale.getDefault(), "%s ordered %d product(s) from %s", customer.getName(), order.getCartItems().size(), shop.getShopName()));
                        }
                    } else {
                        Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                FirebaseFirestore.getInstance().collection("Shops").document(order.getShopId()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        shop = task.getResult().toObject(ModelShop.class);
                        assert shop != null;
                        binding.shopName.setText(shop.getShopName());
                        binding.shopOwnerName.setText(shop.getOwnerName());
                        binding.shopAddress.setText(shop.getAddress());
                        binding.shopPhone.setText(shop.getOwnerPhone());

                        if (customer != null) {
                            binding.orderTitle.setText(String.format(Locale.getDefault(), "%s ordered %d product(s) from %s", customer.getName(), order.getCartItems().size(), shop.getShopName()));
                        }
                    } else {
                        Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                MaterialCardView[] points = {binding.orderPlacedPoint, binding.orderPackedPoint, binding.outForDeliveryPoint, binding.moneyReceivedPoint, binding.deliveredPoint};
                View[] lines = {binding.connectionLine0, binding.connectionLine1, binding.connectionLine2, binding.connectionLine3};
                for (int i=0; i < points.length; i++){
                    if (i < order.getOrderStatus()){
                        points[i].setCardBackgroundColor(Color.GREEN);
                        if (i < lines.length) lines[i].setBackgroundColor(Color.GREEN);
                    } else if (i == order.getOrderStatus()) {
                        points[i].setCardBackgroundColor(Color.GREEN);
                        if (i < lines.length) lines[i].setBackgroundColor(Color.LTGRAY);
                    } else {
                        points[i].setCardBackgroundColor(Color.LTGRAY);
                        if (i < lines.length) lines[i].setBackgroundColor(Color.LTGRAY);
                    }
                }

                if (order.getOrderStatus() == ORDER_PACKED) {
                    binding.orderPickedUp.setVisibility(View.VISIBLE);
                } else {
                    binding.orderPickedUp.setVisibility(View.GONE);
                }

                if (order.getOrderStatus() == OUT_FOR_DELIVERY) {
                    binding.moneyReceived.setVisibility(View.VISIBLE);
                } else {
                    binding.moneyReceived.setVisibility(View.GONE);
                }

                if (order.getOrderStatus() == MONEY_RECEIVED) {
                    binding.secretCodeArea.setVisibility(View.VISIBLE);
                } else {
                    binding.secretCodeArea.setVisibility(View.GONE);
                }

                double subTotal = 0;
                for (ModelCartItem item : order.getCartItems()) {
                    subTotal += (item.getProduct().getOriginalPrice() - (item.getProduct().getOriginalPrice() * item.getProduct().getDiscountPercentage() / 100)) * item.getQuantity() * item.getProduct().getUnitMap().get(item.getUnit());
                }
                binding.subTotal.setText(String.format(Locale.getDefault(), "%.02f", subTotal));
                binding.deliveryCharge.setText(String.format(Locale.getDefault(), "%.02f", order.getDeliveryCharge()));
                binding.grandTotal.setText(String.format(Locale.getDefault(), "%.02f", (subTotal + order.getDeliveryCharge())));
                binding.orderTime.setText(new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date(order.getOrderTime())));
                binding.orderID.setText(order.getOrderId());

            } else {
                Toast.makeText(this, "Sorry, the order may be canceled or not found!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void openMap(double latitude, double longitude) {
        if (latitude != 0.0 && longitude != 0.0) {
            String address = "https://maps.google.com/maps?daddr=" + latitude + "," + longitude;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(address)));
        }
    }

    private void dialPhone(String  phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(phoneNumber))));
    }
}