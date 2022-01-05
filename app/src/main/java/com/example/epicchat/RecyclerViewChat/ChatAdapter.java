package com.example.epicchat.RecyclerViewChat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.ProfileActivity;
import com.example.epicchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>
{
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private List<ChatObject> mChat;
    private Context context;

    FirebaseUser fuser;

    public ChatAdapter(List<ChatObject> usersList, Context context)
    {
        this.mChat = usersList;
        this.context = context;
    }
    @Override
    public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == MSG_TYPE_RIGHT)
        {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chatright_item, null);
            ChatViewHolders rcv = new ChatViewHolders(layoutView);
            return rcv;
        } else {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chatleft_item, null);
            ChatViewHolders rcv = new ChatViewHolders(layoutView);
            return rcv;
        }
    }

    @Override
    public void onBindViewHolder(final ChatViewHolders holder, int position)
    {
        ChatObject chatObject = mChat.get(position);

        holder.show_message.setText(chatObject.getMessage());
    }

    @Override
    public int getItemCount()
    {
        return this.mChat.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}

// Source of Code: https://www.youtube.com/watch?v=1mJv4XxWlu8&list=PLzLFqCABnRQftQQETzoVMuteXzNiXmnj8&index=9 - Chat App with Firebase Part 8 - Displaying Messages - Android Studio Tutorial
// Code was heavily inspired from the tutorial.