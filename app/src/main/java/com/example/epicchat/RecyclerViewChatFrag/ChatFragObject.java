package com.example.epicchat.RecyclerViewChatFrag;

public class ChatFragObject
{
    private String name;
    private String uid;

    public ChatFragObject(String name, String uid)
    {
        this.name = name;
        this.uid = uid;
    }

    public ChatFragObject()
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

