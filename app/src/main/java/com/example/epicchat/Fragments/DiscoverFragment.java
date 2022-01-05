package com.example.epicchat.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.epicchat.R;
import com.example.epicchat.RecyclerViewFollow.FollowAdapter;
import com.example.epicchat.RecyclerViewFollow.FollowObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class DiscoverFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    EditText mInput;

    public static DiscoverFragment newInstance()
    {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        mInput = view.findViewById(R.id.input);
        Button mSearch = view.findViewById(R.id.search);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FollowAdapter(getDataSet(),getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(null);

        mSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clear();
                listenForData();
            }
        });

        return view;
    }

    // Code to listen for data
    private void listenForData()
    {
        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = usersDb.orderByChild("Name").startAt(mInput.getText().toString()).endAt(mInput.getText().toString() + "\uf8ff");
        query.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                String name = "";
                String uid = dataSnapshot.getRef().getKey();
                if(dataSnapshot.child("Name").getValue() != null){
                    name = dataSnapshot.child("Name").getValue().toString();
                }
                if(!name.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                {
                    FollowObject obj = new FollowObject(name, uid);
                    results.add(obj);
                    mRecyclerView.getRecycledViewPool().clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void clear()
    {
        int size = this.results.size();
        this.results.clear();
        mAdapter.notifyItemRangeChanged(0, size);
    }



    private ArrayList<FollowObject> results = new ArrayList<>();
    private ArrayList<FollowObject> getDataSet()
    {
        listenForData();
        return results;
    }
}

// Source of Code - https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/FindUsersActivity.java
// The code is almost entirely copied from the link provided. The only thing changed was that 'email' was changed to 'Name'. This made the search bar search by name rather than email.