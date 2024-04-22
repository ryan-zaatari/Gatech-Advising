package com.example.campusdiscovery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        LoginActivity.currentUser = currentUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        currentUser = null;
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        // startActivity(new Intent(LoginActivity.this, PrimaryActivity.class));

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, PrimaryActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(((EditText)findViewById(R.id.loginUsername)).getText());
                String password = String.valueOf(((EditText)findViewById(R.id.loginPassword)).getText());
                User user = Database.getDB().login(name, password);
                if (user == null) {
                    Snackbar snackbar = Snackbar.make(view, "Couldn't find such user\\password combination", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    setCurrentUser(user);
                    startActivity(new Intent(LoginActivity.this, PrimaryActivity.class));
                }
            }
        });

    }
}
