package com.prasunpersonal.gogroceries_delivery.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.prasunpersonal.gogroceries_delivery.databinding.ActivityScannerBinding;

public class ScannerActivity extends AppCompatActivity {
    ActivityScannerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}