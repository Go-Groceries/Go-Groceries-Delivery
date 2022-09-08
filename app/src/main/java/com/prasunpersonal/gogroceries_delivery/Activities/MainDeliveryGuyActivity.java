package com.prasunpersonal.gogroceries_delivery.Activities;

import static com.prasunpersonal.gogroceries_delivery.App.ME;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.prasunpersonal.gogroceries_delivery.Adapters.AdapterFragment;
import com.prasunpersonal.gogroceries_delivery.Fragments.DeliveryGuyCompletedTasksFragment;
import com.prasunpersonal.gogroceries_delivery.Fragments.DeliveryGuyPendingTasksFragment;
import com.prasunpersonal.gogroceries_delivery.R;
import com.prasunpersonal.gogroceries_delivery.databinding.ActivityMainDeliveryGuyBinding;
import com.prasunpersonal.gogroceries_delivery.databinding.ScannerDialogBinding;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.ScanMode;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainDeliveryGuyActivity extends AppCompatActivity {
    ActivityMainDeliveryGuyBinding binding;
    AlertDialog alertDialog;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDeliveryGuyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.mainDeliveryGuyToolbar.setTitle(ME.getName());
        setSupportActionBar(binding.mainDeliveryGuyToolbar);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new DeliveryGuyPendingTasksFragment());
        fragments.add(new DeliveryGuyCompletedTasksFragment());

        binding.deliveryGuyMainViewpager.setAdapter(new AdapterFragment(getSupportFragmentManager(), getLifecycle(), fragments));
        binding.deliveryGuyMainViewpager.setOffscreenPageLimit(fragments.size());
        binding.deliveryGuyMainViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.deliveryGuyMainTab.selectTab(binding.deliveryGuyMainTab.getTabAt(position));
            }
        });
        binding.deliveryGuyMainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.deliveryGuyMainViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        detectLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_delivery_guy_menu, menu);
        Glide.with(this).load(ME.getDp()).placeholder(R.drawable.ic_person).into((ImageView) menu.findItem(R.id.profile).getActionView().findViewById(R.id.profileMenuBtnPhoto));
        menu.findItem(R.id.profile).getActionView().findViewById(R.id.profileMenuBtnPhoto).setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        ((Switch) menu.findItem(R.id.status).getActionView().findViewById(R.id.statusSwitch)).setChecked(ME.getAvailable());
        ((Switch) menu.findItem(R.id.status).getActionView().findViewById(R.id.statusSwitch)).setOnCheckedChangeListener((buttonView, isChecked) -> FirebaseFirestore.getInstance().collection("DeliveryGuys").document(ME.getUid()).update("available", isChecked));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.scan) {
            checkAndRequestPermission();
        }
        return super.onOptionsItemSelected(item);
    }

    private void detectLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location -> {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    binding.mainDeliveryGuyToolbar.setSubtitle(addresses.get(0).getLocality());
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkAndRequestPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                showScannerDialog();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                if (permissionDeniedResponse.isPermanentlyDenied()) {
                    showSettingsDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).onSameThread().check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_camera);
        builder.setTitle("Camera Permission");
        builder.setMessage("This app needs camera permission. You can enable it in app settings.");
        builder.setCancelable(false);
        builder.setPositiveButton("Settings", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            launcher.launch(intent);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray)));
        alertDialog.show();
    }

    private void showScannerDialog() {
        ScannerDialogBinding dialogBinding = ScannerDialogBinding.inflate(getLayoutInflater());

        AlertDialog dialog = new AlertDialog.Builder(MainDeliveryGuyActivity.this)
                .setCancelable(true)
                .setView(dialogBinding.getRoot())
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        CodeScanner scanner;
        scanner = new CodeScanner(MainDeliveryGuyActivity.this, dialogBinding.scannerView);
        scanner.setCamera(CodeScanner.CAMERA_BACK);
        scanner.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        scanner.setAutoFocusMode(AutoFocusMode.SAFE);
        scanner.setScanMode(ScanMode.SINGLE);
        scanner.setAutoFocusEnabled(true);
        scanner.setFlashEnabled(false);
        scanner.setDecodeCallback(result -> runOnUiThread(()-> {
            startActivity(new Intent(MainDeliveryGuyActivity.this, DeliveryGuyOrderDetailsActivity.class).putExtra("ORDER_ID", result.getText()));
            dialog.dismiss();
        }));
        scanner.setErrorCallback(error -> Toast.makeText(MainDeliveryGuyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show());

        scanner.startPreview();
    }
}