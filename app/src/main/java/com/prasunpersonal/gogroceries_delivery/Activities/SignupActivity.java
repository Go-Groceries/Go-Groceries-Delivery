package com.prasunpersonal.gogroceries_delivery.Activities;

import static com.prasunpersonal.gogroceries_delivery.App.ME;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.prasunpersonal.gogroceries_delivery.Models.ModelDeliveryGuy;
import com.prasunpersonal.gogroceries_delivery.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signupToolbar.setNavigationOnClickListener(v -> finish());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        binding.dSignup.setOnClickListener(v -> {
            if (binding.dName.getText().toString().trim().isEmpty()) {
                binding.dName.setError("Name is required!");
                binding.dName.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.dEmail.getText().toString().trim()).matches()) {
                binding.dEmail.setError("Enter a valid Email address!");
                binding.dEmail.requestFocus();
                return;
            }
            if (!Patterns.PHONE.matcher(binding.dPhone.getText().toString().trim()).matches()) {
                binding.dPhone.setError("Enter a valid phone number!");
                binding.dPhone.requestFocus();
                return;
            }
            if (binding.dPass1.getText().toString().trim().length() < 6) {
                binding.dPass1.setError("Enter a valid password of 6 digits!");
                binding.dPass1.requestFocus();
                return;
            }
            if (!binding.dPass2.getText().toString().equals(binding.dPass1.getText().toString())) {
                binding.dPass2.setError("Passwords doesn't match!");
                binding.dPass2.requestFocus();
                return;
            }

            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.dEmail.getText().toString().trim(),binding.dPass1.getText().toString().trim()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.setMessage("Saving Account Info ...");
                    ME = new ModelDeliveryGuy(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()), binding.dName.getText().toString().trim(), binding.dEmail.getText().toString().trim(), binding.dPhone.getText().toString().trim());
                    FirebaseFirestore.getInstance().collection("DeliveryGuys").document(ME.getUid()).set(ME).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            startActivity(new Intent(this, MainDeliveryGuyActivity.class));
                            progressDialog.dismiss();
                            finishAffinity();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}