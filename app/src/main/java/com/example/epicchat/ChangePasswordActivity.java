package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.epicchat.Fragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity
{
    private EditText oldPsw, newPsw, confirmPsw;
    private Button changePsw;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        oldPsw = findViewById(R.id.oldPassword);
        newPsw = findViewById(R.id.newPassword);
        confirmPsw = findViewById(R.id.confirmPassword);
        changePsw = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.progressBar);
        changePsw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String txtOldPsw = oldPsw.getText().toString();
                String txtNewPsw = newPsw.getText().toString();
                String txtConfirmPsw = confirmPsw.getText().toString();
                if (txtNewPsw.isEmpty() || txtNewPsw.isEmpty() || txtConfirmPsw.isEmpty())
                {
                    Toast.makeText(ChangePasswordActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(txtNewPsw.length() <6){
                    Toast.makeText(ChangePasswordActivity.this, "The new password length should be more than 6 characters", Toast.LENGTH_SHORT).show();
                }
                else if(! txtConfirmPsw.equals(txtNewPsw)){
                    Toast.makeText(ChangePasswordActivity.this, "Confirm password must match the new password", Toast.LENGTH_SHORT).show();
                }
                else{
                    changePassword(txtOldPsw,txtNewPsw);
                }
            }
        });

    }

    private void changePassword(String txtOldPsw, String txtNewPsw)
    {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),txtOldPsw);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful()){
                    firebaseUser.updatePassword(txtNewPsw).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

// Source of Code: https://www.youtube.com/watch?v=BHT8hCtOP1U&t=3047s - Android Login/Registration by Firebase Email Authentication, Realtime Database and Firebase Storage
// The video above was used as a tutorial to create the change password system.