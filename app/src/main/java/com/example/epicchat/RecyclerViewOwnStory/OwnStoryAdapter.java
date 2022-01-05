package com.example.epicchat.RecyclerViewOwnStory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.R;

import java.util.List;

public class OwnStoryAdapter extends RecyclerView.Adapter<OwnStoryViewHolders>
{
    private List<OwnStoryObject> usersList;
    private Context context;

    public OwnStoryAdapter(List<OwnStoryObject> usersList, Context context)
    {
        this.usersList = usersList;
        this.context = context;
    }
    @Override
    public OwnStoryViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_own_story_item, null);
        OwnStoryViewHolders rcv = new OwnStoryViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final OwnStoryViewHolders holder, int position)
    {
        holder.mName.setText(usersList.get(position).getName());
        holder.mName.setTag(usersList.get(position).getUid());
    }

    @Override
    public int getItemCount()
    {
        return this.usersList.size();
    }
}

// Source of Code - https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/RecyclerViewStory/StoryAdapter.java
// The code is taken almost entirely from the link, however 'email' has been changed to 'name'.