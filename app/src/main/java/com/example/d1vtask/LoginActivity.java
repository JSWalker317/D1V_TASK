package com.example.d1vtask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login, btn_newAccount;
    private EditText edtMail, edtPass;

    private FirebaseAuth mAuth;
    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btnLogin);
        btn_newAccount = findViewById(R.id.btnNewAccount);
        edtMail = findViewById(R.id.edt_email_login);
        edtPass = findViewById(R.id.edt_pass_login);

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);
        addListener();

        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }


    }

    private void addListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtMail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    edtMail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edtPass.setError("Password is required");
                    return;
                } else {
                    loader.setMessage("Login in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                edtMail.setText("");
//                                edtPass.setText("");

                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Login failed \n" + error, Toast.LENGTH_SHORT).show();

                            }
                            loader.dismiss();
                        }

                    });
                }
            }
        });

        btn_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}