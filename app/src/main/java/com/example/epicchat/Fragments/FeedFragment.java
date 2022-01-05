package com.example.epicchat.Fragments;

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


import com.example.epicchat.CameraActivity;
import com.example.epicchat.R;
import com.example.epicchat.RecyclerViewStory.StoryAdapter;
import com.example.epicchat.RecyclerViewStory.StoryObject;
import com.example.epicchat.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Toolbar mtoolbar;

    public static FeedFragment newInstance()
    {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StoryAdapter(getDataSet(), getContext());
        mRecyclerView.setAdapter(mAdapter);

            // Toolbar With Stuff
            mtoolbar = (Toolbar) view.findViewById(R.id.toolbar);
            mtoolbar.inflateMenu(R.menu.feed_menu);
            mtoolbar.setTitle("Epic Chat");

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
                    else if(item.getItemId()==R.id.cameraItem)
                    {
                        OpenActivityCamera();
                    }
                    return false;
                }
            });

        return view;
    }

    private void clear()
    {
        int size = this.results.size();
        this.results.clear();
        mAdapter.notifyItemRangeChanged(0, size);
    }

    private ArrayList<StoryObject> results = new ArrayList<>();
    private ArrayList<StoryObject> getDataSet()
    {
        listenForData();
        return results;
    }

    private void listenForData()
    {
        for(int i = 0; i < UserInformation.listFollowing.size(); i++)
        {
            DatabaseReference followingStoryDb = FirebaseDatabase.getInstance().getReference().child("Users").child(UserInformation.listFollowing.get(i));
            followingStoryDb.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String uid = dataSnapshot.getRef().getKey();
                    long timestampBeg = 0;
                    long timestampEnd = 0;
                    for(DataSnapshot storySnapshot : dataSnapshot.child("story").getChildren())
                    {
                        if(storySnapshot.child("timestampBeg").getValue() != null)
                        {
                            timestampBeg = Long.parseLong(storySnapshot.child("timestampBeg").getValue().toString());
                        }
                        if(storySnapshot.child("timestampEnd").getValue() != null)
                        {
                            timestampEnd = Long.parseLong(storySnapshot.child("timestampEnd").getValue().toString());
                        }
                        long timestampCurrent = System.currentTimeMillis();
                        if(timestampCurrent >= timestampBeg && timestampCurrent <= timestampEnd)
                        {
                            StoryObject obj = new StoryObject(name, uid);
                            if(!results.contains(obj))
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
    }

    // Menu Bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.feed_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void OpenActivityCamera()
    {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivity(intent);
    }
}

// Source of Code: https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/fragment/StoryFragment.java
// The 'private void listenForData()' method was inspired from the link above.