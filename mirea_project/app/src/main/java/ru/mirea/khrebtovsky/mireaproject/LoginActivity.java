package ru.mirea.khrebtovsky.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.khrebtovsky.mireaproject.databinding.FragmentLoginActivityBinding;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FragmentLoginActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        binding.signInButton.setOnClickListener(view -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            signIn(email, password);
        });

        binding.createAccountButton.setOnClickListener(view -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            createAccount(email, password);
        });
    }

    private void createAccount(String email, String password) {
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password should be at least 6 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }


    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }


    private static final String TAG = "LoginActivity";
}