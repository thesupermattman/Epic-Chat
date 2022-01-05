package com.example.epicchat.RecyclerViewProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epicchat.DisplayFeedImageActivity;
import com.example.epicchat.DisplayProfileImageActivity;
import com.example.epicchat.R;

public class ProfileViewHolders extends RecyclerView.ViewHolder //implements View.OnClickListener
{
    TextView mDate;
    ImageView mImageView;

    public ProfileViewHolders(@NonNull View itemView)
    {
        super(itemView);

        mDate = itemView.findViewById(R.id.date);
        mImageView = itemView.findViewById(R.id.imageView);
    }
}