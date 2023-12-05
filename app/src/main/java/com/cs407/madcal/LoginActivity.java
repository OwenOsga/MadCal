package com.cs407.madcal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextWiscId;
    private Button buttonLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);
        editTextWiscId = (EditText) findViewById(R.id.editTextWiscId);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyFromSQLite();
            }
        });
    }

    private void verifyFromSQLite() {
        String wiscId = editTextWiscId.getText().toString().trim();

        if (databaseHelper.checkUser(wiscId)) {
            Intent accountsIntent = new Intent(LoginActivity.this, MainActivity.class);
            accountsIntent.putExtra("WISC_ID", wiscId);
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
            showAlertDialog(wiscId);
        }
    }

    private void showAlertDialog(final String wiscId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("WISC ID not found. Would you like to create a new one?")
                .setCancelable(false)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        createUser(wiscId);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createUser(String wiscId) {
        databaseHelper.addUser(wiscId);
        // Optionally, proceed to the main activity or stay on the login page
    }
    private void emptyInputEditText() {
        editTextWiscId.setText(null);
    }
}
