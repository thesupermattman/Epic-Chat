package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity
{
    private EditText Name, Email, Password;
    private Button Register;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        Name = findViewById(R.id.etName);
        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        Register = findViewById(R.id.btnRegisterRegister);
        progressBar = findViewById(R.id.progressBar);

        Register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String name = Name.getText().toString();
                final String email = Email.getText().toString();
                final String txt_password = Password.getText().toString();

                Query nameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Name").equalTo(name);
                nameQuery.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.getChildrenCount()>0){
                            Toast.makeText(RegisterActivity.this, "Name already taken. Please choose a different one", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password)){
                            Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            registerMethod(name,email,txt_password);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }
        });
    }

    private void registerMethod(String name, String email, String txt_password)
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseUser rUser = firebaseAuth.getCurrentUser();
                    assert rUser != null;
                    String userId = rUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("userId", userId);
                    hashMap.put("Name", name);
                    hashMap.put("email", email);
                    hashMap.put("profileImageUrl", "default");
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                                {
                                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

// Source of Code: https://www.youtube.com/watch?v=BHT8hCtOP1U&t=3047s - Android Login/Registration by Firebase Email Authentication, Realtime Database and Firebase Storage
// The video above was used as a tutorial to create the registration system.