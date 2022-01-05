package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.epicchat.RecyclerViewChat.ChatAdapter;
import com.example.epicchat.RecyclerViewChat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity
{
    String userId;

    private FirebaseAuth firebaseAuth;

    private FirebaseUser fuser;
    private DatabaseReference databaseReference;

    ImageButton btn_send;
    EditText text_send;

    private Toolbar mtoolbar;

    // Recyclerview Fields
    ChatAdapter chatAdapter;
    List<ChatObject> mchat;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Bundle for other user
        Bundle b = getIntent().getExtras();
        userId = b.getString("userId");

        // Recyclerview for chat
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        // Send Message
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        btn_send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String msg = text_send.getText().toString();
                if (!msg.equals(""))
                {
                    sendMessage(fuser.getUid(), userId, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty messages", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        // Toolbar with user information
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                UsersData usersData = dataSnapshot.getValue(UsersData.class);
                assert usersData != null;
                mtoolbar.setTitle(usersData.getName());

                readMessagges(fuser.getUid(), userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MessageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Send Message Method
    private void sendMessage(String sender, String receiver, String message)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);

        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(userId);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!dataSnapshot.exists())
                {
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void readMessagges(final String myid, final String userId)
    {
        mchat = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ChatObject chatObject = snapshot.getValue(ChatObject.class);
                    if (chatObject.getReceiver().equals(myid) && chatObject.getSender().equals(userId) ||
                            chatObject.getReceiver().equals(userId) && chatObject.getSender().equals(myid)){
                        mchat.add(chatObject);
                    }

                    chatAdapter = new ChatAdapter(mchat, MessageActivity.this);
                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}

// Source of Code: https://www.youtube.com/watch?v=f1HKTg2hyf0&list=PLzLFqCABnRQftQQETzoVMuteXzNiXmnj8&index=7 - Chat App with Firebase Part 7 - Send Message - Android Studio Tutorial
// Source of Code: https://www.youtube.com/watch?v=1mJv4XxWlu8&list=PLzLFqCABnRQftQQETzoVMuteXzNiXmnj8&index=9 - Chat App with Firebase Part 8 - Displaying Messages - Android Studio Tutorial
// The Youtube tutorials above helped to create the activity.