package com.lasalle.meet.enities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
    @Expose(serialize = true, deserialize = true)
    private String content;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("user_id_send")
    private int userID;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("user_id_recived")
    private int otherUserID;

    public Message(String content, int userID, int otherUserID) {
        this.content = content;
        this.userID = userID;
        this.otherUserID = otherUserID;
    }

    public String getContent() {
        return content;
    }

    public int getUserID() {
        return userID;
    }
}
