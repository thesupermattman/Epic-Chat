package com.example.epicchat.RecyclerViewChatFrag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.MessageActivity;
import com.example.epicchat.ProfileActivity;
import com.example.epicchat.R;

public class ChatFragHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mName;

    public ChatFragHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        mName = itemView.findViewById(R.id.name);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(view.getContext(), MessageActivity.class);
        Bundle b = new Bundle();
        b.putString("userId", mName.getTag().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}