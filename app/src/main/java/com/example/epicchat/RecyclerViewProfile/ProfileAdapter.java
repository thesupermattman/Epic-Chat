package com.example.epicchat.RecyclerViewProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.example.epicchat.R;
import com.example.epicchat.RecyclerViewStory.StoryViewHolders;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolders>
{
    Context context;

    ArrayList<ProfileObject> list;

    public ProfileAdapter(Context context, ArrayList<ProfileObject> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProfileViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_profile_item, null);
        ProfileViewHolders rcv = new ProfileViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolders holder, int position)
    {
        ProfileObject profileObject = list.get(position);
        holder.mDate.setText(profileObject.getDate());

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context.getApplicationContext());
        circularProgressDrawable.setStrokeWidth(5);
        circularProgressDrawable.setCenterRadius(100f);
        circularProgressDrawable.start();

        Picasso.get().load(profileObject.getImageUrl()).placeholder(circularProgressDrawable).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}