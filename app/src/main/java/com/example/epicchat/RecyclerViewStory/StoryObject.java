package com.example.epicchat.RecyclerViewStory;

import java.util.Objects;

public class StoryObject
{
    private String name;
    private String uid;

    public StoryObject(String name, String uid)
    {
        this.name = name;
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }
    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals (Object obj)
    {

        boolean same = false;
        if (obj != null && obj instanceof StoryObject)
        {
            same = this.uid == ((StoryObject) obj).uid;
        }
        return same;
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + (this.uid == null ? 0 : this.uid.hashCode());
        return result;
    }
}

// Source of Code - https://github.com/SimCoderYoutube/SnapchatClone/blob/master/app/src/main/java/com/simcoder/snapchatclone/RecyclerViewStory/StoryObject.java
// The code is taken almost entirely from the link, however 'email' has been changed to 'name'.