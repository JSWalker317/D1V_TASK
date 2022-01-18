package com.example.d1vtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private Button btn_register,btn_back;
    private EditText edt_register_user;
    private EditText edt_register_pass;
    private EditText edt_register_Confirmpass;

//    private EditText edt_name;

    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_register = findViewById(R.id.btn_register);
        btn_back = findViewById(R.id.btn_back);
        edt_register_user = findViewById(R.id.edt_email_register);
        edt_register_pass = findViewById(R.id.edt_pass_register);
        edt_register_Confirmpass = findViewById(R.id.edt_confirmPass_register);
//        edt_name = findViewById(R.id.edt_name_register);

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);
        addListener();

    }

    private void addListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String name = edt_name.getText().toString().trim();
                String username = edt_register_user.getText().toString().trim();
                String password = edt_register_pass.getText().toString().trim();
                String confirm_password = edt_register_Confirmpass.getText().toString().trim();

//
//                if (TextUtils.isEmpty(name)) {
//                    edt_name.setError("Name is required");
//                    return;
//                }
                if (TextUtils.isEmpty(username)) {
                    edt_register_user.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edt_register_pass.setError("Password is required");
                    return;

                }  if (TextUtils.isEmpty(confirm_password)) {
                    edt_register_Confirmpass.setError("Confirm_Password is required");
                    return;
                } if(!password.equals(confirm_password)) {
                    edt_register_Confirmpass.setError("Confirm_Password is not Correct");
                    return;
                }
                else {
                    loader.setMessage("Register in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Login failed \n" + error, Toast.LENGTH_SHORT).show();

                            }
                            loader.dismiss();
                        }
                    });

                }
            }
        });
    }


}