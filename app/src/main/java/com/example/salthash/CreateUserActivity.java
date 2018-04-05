package com.example.salthash;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class CreateUserActivity extends AppCompatActivity {

    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        TextView user = findViewById(R.id.userLabel);
        TextView pass = findViewById(R.id.passLabel);
        Button submit = findViewById(R.id.createButton);
        user.setText("Username");
        pass.setText("Password");
        submit.setText("Submit");
    }

    public void createUser(View view) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        EditText u = findViewById(R.id.userText);
        String user = u.getText().toString();
        EditText p = findViewById(R.id.passText);
        String pass = p.getText().toString();
        Context c = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_LONG;
        Toast toast;
        db.insertUser(new User(user, pass));
        text = "User created!";
        toast = Toast.makeText(c, text, duration);
        toast.show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
