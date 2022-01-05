package com.example.epicchat.RecyclerViewProfile;

public class ProfileObject
{
    String date, imageUrl;

    ProfileObject()
    {

    }

    public ProfileObject(String date, String imageUrl)
    {
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}