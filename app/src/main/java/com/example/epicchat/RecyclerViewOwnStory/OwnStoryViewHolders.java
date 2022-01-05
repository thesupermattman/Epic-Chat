package com.example.epicchat.RecyclerViewOwnStory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.DisplayFeedImageActivity;
import com.example.epicchat.DisplayProfileImageActivity;
import com.example.epicchat.R;

public class OwnStoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView mName;

    public OwnStoryViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        mName = itemView.findViewById(R.id.name);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(view.getContext(), DisplayProfileImageActivity.class);
        Bundle b = new Bundle();
        b.putString("userId", mName.getTag().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}

// Source of Code - https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/RecyclerViewStory/StoryViewHolders.java
// The code is taken almost entirely from the link, however 'email' has been changed to 'name'.