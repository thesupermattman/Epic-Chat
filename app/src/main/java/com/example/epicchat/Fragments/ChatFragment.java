package com.example.epicchat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.epicchat.Chatlist;
import com.example.epicchat.R;
import com.example.epicchat.RecyclerViewChatFrag.ChatFragAdapter;
import com.example.epicchat.RecyclerViewChatFrag.ChatFragObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment
{
    private Toolbar mtoolbar;

    EditText mInput;

    private RecyclerView recyclerView;

    private ChatFragAdapter userAdapter;
    private List<ChatFragObject> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<Chatlist> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mInput = view.findViewById(R.id.input);
        Button mSearch = view.findViewById(R.id.search);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        mSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clear();
                chatList();
            }
        });

        return view;
    }

    private void chatList()
    {
        mUsers = new ArrayList<>();
        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = usersDb.orderByChild("Name").startAt(mInput.getText().toString()).endAt(mInput.getText().toString() + "\uf8ff");
        query.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                String name = "";
                String uid = dataSnapshot.getRef().getKey();
                for (Chatlist chatlist : usersList)
                {
                    if(dataSnapshot.child("Name").getValue() != null && dataSnapshot.child("userId").getValue().equals(chatlist.getId()))
                    {
                        name = dataSnapshot.child("Name").getValue().toString();
                        ChatFragObject obj = new ChatFragObject(name, uid);
                        mUsers.add(obj);
                    }
                }

                userAdapter = new ChatFragAdapter(mUsers, getContext());
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void clear()
    {
        int size = this.mUsers.size();
        this.mUsers.clear();
        userAdapter.notifyItemRangeChanged(0, size);
    }
}

// Source of Code: https://www.youtube.com/watch?v=Dl_IFwldjWk&list=PLzLFqCABnRQftQQETzoVMuteXzNiXmnj8&index=9 - Chat App with Firebase Part 9 - Display Users with whom you have Messages - Android Studio Tutorial
// Code was heavily inspired from the tutorial. But many changes had to be made so that the active chats could be added to this screen after starting the conversation from discovery.