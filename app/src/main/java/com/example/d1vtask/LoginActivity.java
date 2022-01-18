package com.example.d1vtask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login, btn_newAccount;
    private EditText edtMail, edtPass;
    private CheckBox login_checkbox;
    private TextView tv_forgot_pass;

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
        login_checkbox = findViewById(R.id.login_checkbox);
        tv_forgot_pass = findViewById(R.id.tv_forgot_pass);


        mAuth = FirebaseAuth.getInstance();

        login_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        loader = new ProgressDialog(this);
        addListener();

        if (mAuth.getCurrentUser() != null) {
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

        tv_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtMail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    edtMail.setError("Email is required to reset");
                    Toast.makeText(LoginActivity.this, "Please give email to reset! \n", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Reset Password")
                            .setMessage("Are you sure to reset password ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.sendPasswordResetEmail(email)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(LoginActivity.this, "Reset link sent \n", Toast.LENGTH_SHORT).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginActivity.this, "Error \n" + e, Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                }
                            });
                    builder.create();
                    builder.show();
                }


            }
        });
    }
}