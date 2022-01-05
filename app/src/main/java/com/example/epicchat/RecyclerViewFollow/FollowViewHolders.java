package com.example.epicchat.RecyclerViewFollow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.DisplayFeedImageActivity;
import com.example.epicchat.ProfileActivity;
import com.example.epicchat.R;

public class FollowViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView mName;
    public Button mFollow;
    public ImageButton mChat;

    public FollowViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        mName = itemView.findViewById(R.id.name);
        mFollow = itemView.findViewById(R.id.follow);
        mChat = itemView.findViewById(R.id.chat);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(view.getContext(), ProfileActivity.class);
        Bundle b = new Bundle();
        b.putString("userId", mName.getTag().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}