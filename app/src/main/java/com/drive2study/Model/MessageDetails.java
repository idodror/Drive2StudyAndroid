package com.drive2study.Model;

import java.util.HashMap;
import java.util.Map;

public class MessageDetails {
    public static String username;
    public static String chatWith;
    public static String message;

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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("chatWith", chatWith);
        result.put("message", message);

        return result;
    }
}
