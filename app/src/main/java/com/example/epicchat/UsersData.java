package com.example.epicchat;

public class UsersData
{
    private String userId;
    private String Name;
    private String email;
    private String profileImageUrl;
    private String date;

    public UsersData(String userId, String name, String email, String profileImageUrl, String date)
    {
        this.userId = userId;
        Name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.date = date;
    }

    public UsersData()
    {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImageUrl(String profileImageUrl)
    {
        this.profileImageUrl = profileImageUrl;
    }
}
