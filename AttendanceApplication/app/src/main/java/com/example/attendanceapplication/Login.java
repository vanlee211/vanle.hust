package com.example.attendanceapplication;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private LinearLayout layoutSignup;
    private EditText editUser,editPass;
    private Button signIn;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layoutSignup = findViewById(R.id.layout_signup);
        progressDialog = new ProgressDialog(this);
        signIn = (Button) findViewById(R.id.btnlogin);
        editUser =(EditText)findViewById(R.id.edit_user);
        editPass =(EditText)findViewById(R.id.edit_pass);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editUser.getText().toString().trim();
                String password = editPass.getText().toString().trim();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(user, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(Login.this,MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        layoutSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void forgotpasspage(View view) {
        
    }
}