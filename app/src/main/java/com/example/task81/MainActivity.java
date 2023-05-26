package com.example.task81;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonSignUp;
    private DatabaseHelper databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database helper
        databaseManager = new DatabaseHelper(this);
        databaseManager.open();

        // Initialize the views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Set OnClickListener for the Login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered username and password
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Validate the credentials using the database manager
                boolean isValidCredentials = databaseManager.validateUserCredentials(username, password);

                if (isValidCredentials) {
                    // Navigate to the HomeActivity if credentials are valid
                    navigateToHomeActivity();
                } else {
                    // Show a toast message for invalid credentials
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set OnClickListener for the Sign Up button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the SignUpActivity when Sign Up button is clicked
                navigateToSignUpActivity();
            }
        });
    }

    // Method to navigate to the HomeActivity
    private void navigateToHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    // Method to navigate to the SignUpActivity
    private void navigateToSignUpActivity() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed
        databaseManager.close();
    }
}
