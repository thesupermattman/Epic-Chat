package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserInformation
{

    public static ArrayList<String> listFollowing = new ArrayList<>();

    public void startFetching()
    {
        listFollowing.clear();
        getUserFollowing();
    }

    private void getUserFollowing()
    {
        DatabaseReference userFollowingDB = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("following");
        userFollowingDB.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if (dataSnapshot.exists())
                {
                    String uid = dataSnapshot.getRef().getKey();
                    if(uid != null && !listFollowing.contains(uid))
                    {
                        listFollowing.add(uid);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String uid = dataSnapshot.getRef().getKey();
                    if(uid != null)
                    {
                        listFollowing.remove(uid);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName)
            {
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
            }
        });
    }
}

// Source of Code - https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/UserInformation.java
// The code is entirely taken from the link above.