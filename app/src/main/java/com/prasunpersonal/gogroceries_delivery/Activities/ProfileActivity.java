package com.prasunpersonal.gogroceries_delivery.Activities;

import static com.prasunpersonal.gogroceries_delivery.App.ME;
import static com.prasunpersonal.gogroceries_delivery.App.PROGRESS_NOTIFICATION_ID;
import static com.prasunpersonal.gogroceries_delivery.App.getFileSize;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.prasunpersonal.gogroceries_delivery.Models.ModelDeliveryGuy;
import com.prasunpersonal.gogroceries_delivery.R;
import com.prasunpersonal.gogroceries_delivery.databinding.ActivityProfileBinding;
import com.prasunpersonal.gogroceries_delivery.databinding.ImgOptionsBinding;
import com.prasunpersonal.gogroceries_delivery.databinding.ShowImageBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    ActivityResultLauncher<Intent> editImageResponse = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            showImg(result.getData().getData().toString(), false, true);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.profileToolbar);
        binding.profileToolbar.setNavigationOnClickListener(v -> finish());

        FirebaseFirestore.getInstance().collection("DeliveryGuys").document(ME.getUid()).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (value != null) {
                ME = value.toObject(ModelDeliveryGuy.class);
                Glide.with(this).load(ME.getDp()).placeholder(R.drawable.ic_person).into(binding.profileDp);
                binding.profileName.setText(ME.getName());
                binding.profilePhone.setText(ME.getPhone());
                binding.profileEmail.setText(ME.getEmail());
            } else {
                Toast.makeText(this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        binding.profileDp.setOnClickListener(v -> {
            ImgOptionsBinding dialogBinding = ImgOptionsBinding.inflate(getLayoutInflater());
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setContentView(dialogBinding.getRoot());

            if (ME.getDp() == null){
                dialogBinding.deleteImg.setVisibility(View.GONE);
                dialogBinding.viewImg.setVisibility(View.GONE);
            }else {
                dialogBinding.deleteImg.setVisibility(View.VISIBLE);
                dialogBinding.viewImg.setVisibility(View.VISIBLE);
            }

            dialogBinding.editImg.setOnClickListener(v1 -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                editImageResponse.launch(intent);
                dialog.dismiss();
            });

            dialogBinding.viewImg.setOnClickListener(v1 -> {
                showImg(ME.getDp(), true, false);
                dialog.dismiss();
            });

            dialogBinding.deleteImg.setOnClickListener(v1 -> {
                dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Delete profile photo?")
                        .setMessage("Are you sure about this process?")
                        .setPositiveButton("Delete", (dialog1, which) -> {
                            dialog1.dismiss();
                            FirebaseFirestore.getInstance().collection("DeliveryGuys").document(ME.getUid()).update("dp", null).addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(this, "Profile photo deleted successfully.", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(this, "Can't delete profile photo: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());
                AlertDialog deleteAlert = builder.create();
                deleteAlert.setOnShowListener(dialog13 -> deleteAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray)));
                deleteAlert.show();
            });
            dialog.show();
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImg(String uri, boolean cancelable, boolean editable){
        ShowImageBinding dialogBinding = ShowImageBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .setCancelable(cancelable);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(ActivityCompat.getDrawable(this, R.drawable.dialog_bg));
        dialog.show();

        if (editable){
            dialogBinding.editableArea.setVisibility(View.VISIBLE);
        }else {
            dialogBinding.editableArea.setVisibility(View.GONE);
        }

        Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.ic_person).into(dialogBinding.showImg);
        dialogBinding.btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialogBinding.btnUpload.setOnClickListener(v -> {
            Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show();
            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, PROGRESS_NOTIFICATION_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Uploading image")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true);

            NotificationManagerCompat notificationManager;
            notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, notificationBuilder.build());

            StorageReference storageRef = FirebaseStorage.getInstance().getReference(getResources().getString(R.string.app_name)).child("ProfileImages").child(ME.getUid());
            storageRef.putFile(Uri.parse(uri)).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        FirebaseFirestore.getInstance().collection("DeliveryGuys").document(ME.getUid()).update("dp", uri1.toString()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                Toast.makeText(this, "Profile photo updated successfully.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(this, "Cant update profile photo: " + Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                }
                notificationManager.cancel(1);
            }).addOnProgressListener(snapshot -> {
                notificationBuilder.setContentText(getFileSize(snapshot.getBytesTransferred()) + "/" + getFileSize(snapshot.getTotalByteCount()))
                        .setProgress((int) snapshot.getTotalByteCount(), (int) snapshot.getBytesTransferred(), false);
                notificationManager.notify(1, notificationBuilder.build());
            });
            dialog.dismiss();
        });
    }

}