package com.example.task81;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextFullName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonCreateAccount;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.open();

        // Initialize the views
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        // Set OnClickListener for the Create Account button
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered input values
                String fullName = editTextFullName.getText().toString();
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                // Validate the input values
                if (validateInput(fullName, username, password, confirmPassword)) {
                    // Check if the username already exists in the database
                    if (databaseHelper.checkUserExistence(username)) {
                        Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        // Add the user to the database and show a success message
                        databaseHelper.addUser(fullName, username, password);
                        Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

    // Method to validate the input fields
    private boolean validateInput(String fullName, String username, String password, String confirmPassword) {
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed
        databaseHelper.close();
    }
}
