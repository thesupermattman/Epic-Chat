package com.example.epicchat.RecyclerViewStory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.DisplayFeedImageActivity;
import com.example.epicchat.R;

public class StoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView mName;
    public ImageButton mProfileButton;

    public StoryViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        mName = itemView.findViewById(R.id.name);
        mProfileButton = itemView.findViewById(R.id.profileButton);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(view.getContext(), DisplayFeedImageActivity.class);
        Bundle b = new Bundle(); // Here is where I make a bundle to send the user that I want.
        b.putString("userId", mName.getTag().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}

// Source of Code - https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/RecyclerViewStory/StoryViewHolders.java
// The code is taken almost entirely from the link, however 'email' has been changed to 'name'.
// Also, the profile button was added by me.