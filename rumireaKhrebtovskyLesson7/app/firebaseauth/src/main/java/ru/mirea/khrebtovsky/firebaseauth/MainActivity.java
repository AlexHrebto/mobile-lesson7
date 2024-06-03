package ru.mirea.khrebtovsky.firebaseauth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private TextView statusTextView;
    private Button signInButton, createAccountButton, signOutButton, verifyEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        statusTextView = findViewById(R.id.statusTextView);
        signInButton = findViewById(R.id.signInButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        signOutButton = findViewById(R.id.signOutButton);
        verifyEmailButton = findViewById(R.id.verifyEmailButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            verifyEmailButton.setEnabled(false);
            user.sendEmailVerification()
                    .addOnCompleteListener(this, task -> {
                        verifyEmailButton.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            statusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
            signOutButton.setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);
            verifyEmailButton.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
            createAccountButton.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            passwordEditText.setVisibility(View.GONE);
        } else {
            statusTextView.setText(R.string.signed_out);
            signOutButton.setVisibility(View.GONE);
            verifyEmailButton.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            createAccountButton.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        if (email.isEmpty()) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (password.isEmpty()) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }
}