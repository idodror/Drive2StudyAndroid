package com.drive2study.Model.Objects;

import java.util.HashMap;
import java.util.Map;

public class MessageDetails {
    public String username;
    public String chatWith;
    public String message;
    public String type;
    public String date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MessageDetails(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatWith() {
        return chatWith;
    }

    public void setChatWith(String chatWith) {
        this.chatWith = chatWith;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) { this.date = date; }

    public String getDate() { return date; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("chatWith", chatWith);
        result.put("message", message);
        result.put("username", username);
        result.put("type", type);
        result.put("date", date);
        return result;
    }



}
