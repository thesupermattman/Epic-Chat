package com.example.epicchat.RecyclerViewFollow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.MessageActivity;
import com.example.epicchat.ProfileActivity;
import com.example.epicchat.R;
import com.example.epicchat.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowViewHolders>{

    private List<FollowObject> usersList;
    private Context context;

    public FollowAdapter(List<FollowObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }
    @Override
    public FollowViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_followers_item, null);
        FollowViewHolders rcv = new FollowViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final FollowViewHolders holder, int position)
    {
        holder.mName.setText(usersList.get(position).getName());
        holder.mName.setTag(usersList.get(position).getUid()); // Tag that I need to set for the bundle
        
        if(UserInformation.listFollowing.contains(usersList.get(holder.getLayoutPosition()).getUid())){
            holder.mFollow.setText("following");
        }else{
            holder.mFollow.setText("follow");
        }

        holder.mFollow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(!UserInformation.listFollowing.contains(usersList.get(holder.getLayoutPosition()).getUid()))
                {
                    holder.mFollow.setText("following");
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("following").child(usersList.get(holder.getLayoutPosition()).getUid()).setValue(true);
                }else{
                    holder.mFollow.setText("follow");
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("following").child(usersList.get(holder.getLayoutPosition()).getUid()).removeValue();
                }
            }
        });

        holder.mChat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                Bundle b = new Bundle();
                b.putString("userId", holder.mName.getTag().toString());
                intent.putExtras(b);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }
}

// Source of Code - https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/RecyclerViewFollow/FollowAdapater.java
// Most of the code is taken from the link above apart from the 'holder.mChat.setOnClickListener(new View.OnClickListener()' method.