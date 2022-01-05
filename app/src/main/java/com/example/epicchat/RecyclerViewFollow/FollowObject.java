package com.example.epicchat.RecyclerViewFollow;

import java.util.Objects;

public class FollowObject
{
    private String name;
    private String uid;

    public FollowObject(String name, String uid)
    {
        this.name = name;
        this.uid = uid;
    }

    public FollowObject()
    {

    }

    public String getUid(){
        return uid;
    }
    public void setUid(String uid){
        this.uid = uid;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
}