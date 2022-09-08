package com.prasunpersonal.gogroceries_delivery.Activities;

import static com.prasunpersonal.gogroceries_delivery.App.ME;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prasunpersonal.gogroceries_delivery.Models.ModelDeliveryGuy;
import com.prasunpersonal.gogroceries_delivery.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.loginToolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        binding.Forgot.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        binding.Register.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

        binding.Login.setOnClickListener(v -> {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.getText().toString().trim()).matches()){
                binding.emailEt.setError("Enter a valid email!");
                binding.emailEt.requestFocus();
                return;
            }
            if (binding.passwordEt.getText().toString().trim().length() < 6){
                binding.passwordEt.setError("Enter a valid password!");
                binding.passwordEt.requestFocus();
                return;
            }

            progressDialog.setMessage("Logging In ...");
            progressDialog.show();

            FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.emailEt.getText().toString().trim(), binding.passwordEt.getText().toString().trim()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseFirestore.getInstance().collection("DeliveryGuys").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            ME = task1.getResult().toObject(ModelDeliveryGuy.class);
                            assert ME != null;
                            progressDialog.dismiss();
                            startActivity(new Intent(this, MainDeliveryGuyActivity.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}