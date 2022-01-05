package com.example.epicchat.RecyclerViewChatFrag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.MessageActivity;
import com.example.epicchat.R;
import com.example.epicchat.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatFragAdapter extends RecyclerView.Adapter<ChatFragHolders>{

    private List<ChatFragObject> usersList;
    private Context context;

    public ChatFragAdapter(List<ChatFragObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }
    @Override
    public ChatFragHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_chat_frag_item, null);
        ChatFragHolders rcv = new ChatFragHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ChatFragHolders holder, int position)
    {
        holder.mName.setText(usersList.get(position).getName());
        holder.mName.setTag(usersList.get(position).getUid()); // Tag that I need to set for the bundle
    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }
}