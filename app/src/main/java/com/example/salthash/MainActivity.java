package com.example.salthash;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView user = findViewById(R.id.userLabel);
        user.setText("Username");
        TextView pass = findViewById(R.id.passLabel);
        pass.setText("Password");
        String s = "abcd1234";
        byte[] b = s.getBytes();
        System.out.println("byte[] " + new String(b));
        char[] c = new char[b.length];
        for(int i = 0; i < b.length; i++)
            c[i] = (char) b[i];
        System.out.println("char[] " + new String(c));
    }

    // Called when user taps send button
    public void submitPassword(View view) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        EditText u = findViewById(R.id.loginUser);
        String user = u.getText().toString();
        Context c = getApplicationContext();
        CharSequence text = "Invalid username / password.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(c, text, duration);
        EditText p = findViewById(R.id.submitPassword);
        String pass = p.getText().toString();
        String[] s = db.checkUser(user);
        if(!s[0].equals("NULL")) {
            byte[] submitHash = User.hash(s[1].getBytes("UTF8"), pass);
            String subHash = new String(submitHash, "UTF8");
            System.out.println("Entered Pass Hash: " + subHash);
            System.out.println("Pass from db: " + s[0]);
            if(subHash.equals(s[0])) {//if(Arrays.equals(submitHash, s[0].getBytes())) {
                Intent intent = new Intent(this, DisplayMessageActivity.class);
                startActivity(intent);
            }
            else {
                toast.show();
            }
        }
        else {
            toast.show();
        }
    }

    public void displayCreateScreen(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }
}
