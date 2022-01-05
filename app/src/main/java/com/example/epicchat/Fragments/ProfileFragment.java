package com.example.epicchat.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epicchat.ChangePasswordActivity;
import com.example.epicchat.MainActivity;
import com.example.epicchat.R;
import com.example.epicchat.RecyclerViewOwnStory.OwnStoryAdapter;
import com.example.epicchat.RecyclerViewOwnStory.OwnStoryObject;
import com.example.epicchat.RecyclerViewProfile.ProfileAdapter;
import com.example.epicchat.RecyclerViewProfile.ProfileObject;
import com.example.epicchat.UsersData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileFragment extends Fragment
{

    private Activity referenceActivity;
    private Button Logout;
    private TextView userName;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    DatabaseReference database;
    ArrayList<ProfileObject> list;
    private Toolbar mtoolbar;

    // Recyclerview Fields
    private RecyclerView recyclerView;
    private ProfileAdapter myAdapter;

    // Recyclerview for own stories fields
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Profile Bar
        userName = (TextView) view.findViewById(R.id.name);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                UsersData usersData = dataSnapshot.getValue(UsersData.class);
                assert usersData != null;
                mtoolbar.setTitle(usersData.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Recyclerview for profile post
        recyclerView = view.findViewById(R.id.recview);
        database = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("story");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new ProfileAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        // Code to reverse the order of the recyclerview
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        // Code to add things to the recyclerview
        database.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ProfileObject profileObject = dataSnapshot.getValue(ProfileObject.class);
                    list.add(profileObject);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // Recyclerview for own stories
        mRecyclerView = view.findViewById(R.id.recview2);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OwnStoryAdapter(getDataSet(), getContext());
        mRecyclerView.setAdapter(mAdapter);

        // Toolbar With Stuff
        mtoolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mtoolbar.inflateMenu(R.menu.profile_menu);

        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if(item.getItemId()==R.id.refreshItem)
                {
                    clear();
                    listenForData();
                }
                else if(item.getItemId()==R.id.btnLogout)
                {
                    LogOut();
                }
                else if(item.getItemId()==R.id.btnChangePassword)
                {
                    openActivityChangePassword();
                }
                return false;
            }
        });

        return view;
    }

    // Log Out Method
    private void LogOut()
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return;
    }

    private void clear()
    {
        int size = this.results.size();
        this.results.clear();
        mAdapter.notifyItemRangeChanged(0, size);
    }

    private ArrayList<OwnStoryObject> results = new ArrayList<>();
    private ArrayList<OwnStoryObject> getDataSet()
    {
        listenForData();
        return results;
    }

    // Method for own stories recyclerview
    private void listenForData()
    {
        DatabaseReference followingStoryDb = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
        followingStoryDb.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String email = dataSnapshot.child("Name").getValue().toString();
                String uid = dataSnapshot.getRef().getKey();
                long timestampBeg = 0;
                long timestampEnd = 0;
                for (DataSnapshot storySnapshot : dataSnapshot.child("story").getChildren())
                {
                    if (storySnapshot.child("timestampBeg").getValue() != null)
                    {
                        timestampBeg = Long.parseLong(storySnapshot.child("timestampBeg").getValue().toString());
                    }
                    if (storySnapshot.child("timestampEnd").getValue() != null)
                    {
                        timestampEnd = Long.parseLong(storySnapshot.child("timestampEnd").getValue().toString());
                    }
                    long timestampCurrent = System.currentTimeMillis();
                    if (timestampCurrent >= timestampBeg && timestampCurrent <= timestampEnd)
                    {
                        OwnStoryObject obj = new OwnStoryObject(email, uid);
                        if (!results.contains(obj))
                        {
                            results.add(obj);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    // Menu Bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void openActivityChangePassword()
    {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(intent);
    }
}

// Source of Code: https://www.youtube.com/watch?v=M8sKwoVjqU0&t=913s - Firebase Data to RecyclerView | How to Retrieve Firebase Data into Recyclerview | Android Studio
// The link above helped to get an understanding of how to put the profile items into the RecyclerView.

// Source of Code: https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/fragment/StoryFragment.java
// The 'private void listenForData()' method was inspired from the link above.