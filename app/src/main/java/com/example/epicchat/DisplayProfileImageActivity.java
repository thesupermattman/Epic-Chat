package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayProfileImageActivity extends AppCompatActivity
{
    String userId;
    private ImageView mImage;
    private boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile_image);

        Bundle b = getIntent().getExtras();
        userId = b.getString("userId");

        mImage = findViewById(R.id.image);

        listenForData();
    }

    ArrayList<String> imageUrlList = new ArrayList<>();

    private void listenForData()
    {
        DatabaseReference followingStoryDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        followingStoryDb.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String imageUrl = "";
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
                    if(storySnapshot.child("imageUrl").getValue() != null)
                    {
                        imageUrl = (storySnapshot.child("imageUrl").getValue().toString());
                    }
                    long timestampCurrent = System.currentTimeMillis();
                    if(timestampCurrent >= timestampBeg && timestampCurrent <= timestampEnd)
                    {
                        imageUrlList.add(imageUrl);
                        if(!started)
                        {
                            started = true;
                            initializeDisplay();
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

    private int imageIterator = 0;

    private void initializeDisplay()
    {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5);
        circularProgressDrawable.setCenterRadius(100f);
        circularProgressDrawable.start();

        Glide.with(getApplication()).load(imageUrlList.get(imageIterator)).placeholder(circularProgressDrawable).into(mImage);

        mImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeImage();
            }
        });

        final Handler handler = new Handler();
        final int delay = 100000;

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                changeImage();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void changeImage()
    {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5);
        circularProgressDrawable.setCenterRadius(100f);
        circularProgressDrawable.start();

        if(imageIterator == imageUrlList.size() - 1)
        {
            finish();
            return;
        }
        imageIterator++;
        Glide.with(getApplication()).load(imageUrlList.get(imageIterator)).placeholder(circularProgressDrawable).into(mImage);
    }
}

// Source of Code: https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/DisplayImageActivity.java
// The code was taken almost entirely from the link above, however, the CircularProgressDrawable was added by me.