package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText Email, Password;
    // private TextView forgetPassword;
    private FirebaseAuth firebaseAuth;
    private TextView forgotTextLink;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button Login = findViewById(R.id.btnLoginLogin);
        Email = findViewById(R.id.etLoginEmail);
        Password = findViewById(R.id.etLoginPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        forgotTextLink = findViewById(R.id.forgetPassword);
        progressBar = findViewById(R.id.progressBar);

        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String tex_email = Email.getText().toString();
                String tex_password = Password.getText().toString();
                if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
                    Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }
                else
                    loginMethod(tex_email,tex_password);
            }
        });

        // Forget Password Code
        forgotTextLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to receive a reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link

                        String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error! Reset link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }

    private void loginMethod(String tex_email, String tex_password)
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(tex_email,tex_password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

// Source of Code: https://www.youtube.com/watch?v=BHT8hCtOP1U&t=3047s - Android Login/Registration by Firebase Email Authentication, Realtime Database and Firebase Storage
// The video above was used as a tutorial to create the login system.