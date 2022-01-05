package com.example.epicchat.RecyclerViewStory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.HomeActivity;
import com.example.epicchat.ProfileActivity;
import com.example.epicchat.R;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryViewHolders>
{
    private List<StoryObject> usersList;
    private Context context;

    public StoryAdapter(List<StoryObject> usersList, Context context)
    {
        this.usersList = usersList;
        this.context = context;
    }
    @Override
    public StoryViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_story_item, null);
        StoryViewHolders rcv = new StoryViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final StoryViewHolders holder, int position)
    {
        holder.mName.setText(usersList.get(position).getName());
        holder.mName.setTag(usersList.get(position).getUid());

        holder.mProfileButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("userId", holder.mName.getTag().toString());
                intent.putExtras(b);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return this.usersList.size();
    }
}

// Source of Code - https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/RecyclerViewStory/StoryAdapter.java
// The code is inspired by the link, however, the 'holder.mProfileButton.setOnClickListener(new View.OnClickListener()' is added by me.