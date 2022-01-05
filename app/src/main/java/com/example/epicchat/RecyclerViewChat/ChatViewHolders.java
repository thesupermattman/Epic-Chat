package com.example.epicchat.RecyclerViewChat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.DisplayFeedImageActivity;
import com.example.epicchat.R;

public class ChatViewHolders extends RecyclerView.ViewHolder
{
    public TextView show_message;

    public ChatViewHolders(View itemView)
    {
        super(itemView);
        show_message = itemView.findViewById(R.id.show_message);
    }
}