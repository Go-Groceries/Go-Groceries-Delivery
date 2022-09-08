package com.prasunpersonal.gogroceries_delivery.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prasunpersonal.gogroceries_delivery.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.forgotPasswordToolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        binding.forgotPasswordToolbar.setNavigationOnClickListener(v -> finish());
        binding.recover.setOnClickListener(v -> {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.getText().toString().trim()).matches()){
                binding.emailEt.setError("Please enter a valid email!");
                binding.emailEt.requestFocus();
                return;
            }

            progressDialog.setMessage("Sending password reset link.");
            progressDialog.show();

            FirebaseAuth.getInstance().sendPasswordResetEmail(binding.emailEt.getText().toString().trim()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this,"Password reset link has been send to your email address.",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}