package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epicchat.RecyclerViewOwnStory.OwnStoryAdapter;
import com.example.epicchat.RecyclerViewOwnStory.OwnStoryObject;
import com.example.epicchat.RecyclerViewProfile.ProfileAdapter;
import com.example.epicchat.RecyclerViewProfile.ProfileObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity
{
    String userId;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Bundle that I got from StoryViewHolders to get the Uid of the user
        Bundle b = getIntent().getExtras();
        userId = b.getString("userId");

        // Profile Bar
        userName = (TextView) findViewById(R.id.name);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
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
                Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Recyclerview for profile post
        recyclerView = findViewById(R.id.recview);
        //database = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("story");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new ProfileAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        // Code to reverse the order of the recyclerview
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("story"); // userId is the ID that I got from the bundle
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
        mRecyclerView = findViewById(R.id.recview2);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OwnStoryAdapter(getDataSet(), this);
        mRecyclerView.setAdapter(mAdapter);

        // Toolbar With Stuff
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.inflateMenu(R.menu.other_profile_menu);

        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if (item.getItemId() == R.id.refreshItem)
                {
                    clear();
                    listenForData();
                }
                return false;
            }
        });
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
        DatabaseReference followingStoryDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.other_profile_menu, menu);
        return true;
    }
}

// Source of Code: https://www.youtube.com/watch?v=M8sKwoVjqU0&t=913s - Firebase Data to RecyclerView | How to Retrieve Firebase Data into Recyclerview | Android Studio
// The link above helped to get an understanding of how to put the profile items into the RecyclerView.

// Source of Code: https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/fragment/StoryFragment.java
// The 'private void listenForData()' method was inspired from the link above.